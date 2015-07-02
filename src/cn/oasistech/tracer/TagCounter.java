package cn.oasistech.tracer;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TagCounter extends TagStatistician {
    private static final Logger logger = Logger.getLogger(TagCounter.class);
            
    public class Result extends TagStatistician.Result {
        private int deep; // 树节点的深度
        private Node<Object, Integer> root;  // 树根节点，保存总计数

        public Result() {
            root = new Node<Object, Integer>();
            root.setKey("total");
            root.setValue(0);
            deep = 0;
        }
        
        @Override
        public String format() {
            StringBuilder str = new StringBuilder();
            str.append(getName()).append(": ");
            for (int i = 0; i <= deep; i++) {
                str.append(format(i));
            }
            return str.toString();
        }
        
        @Override
        public String format(int tier) {
            if (tier > deep) {
                logger.error("tag counter error: format tier > deep");
                return "";
            }
            
            List<Node<Object, Integer>> parents = new ArrayList<Node<Object, Integer>>();
            StringBuilder str = new StringBuilder();
            format(root, parents, tier, str);
            return str.toString();
        }
        
        @Override
        public String formatLastTier() {
            return format(deep);
        }
        
        private void format(Node<Object, Integer> node, List<Node<Object, Integer>> parents, int tier, StringBuilder str) {
            if (parents.size() == tier) {
                formatElement(node, parents, str);
                if (tier == 0) {
                    str.append("\r\n");
                }
                return;
            }
            
            parents.add(node);
            for (Node<Object, Integer> n : node.getNodes().values()) {
                format(n, parents, tier, str);
            }
            parents.remove(node);
            
            // remove last ,
            if (node.getNodes().isEmpty() == false) {
                if (str.length() > ", ".length()) {
                    str.setLength(str.length() - ", ".length());
                }
            }
            
            // newline every child nodes of parent
            str.append("\r\n");
        }
        
        private boolean formatElement(Node<Object, Integer> node, List<Node<Object, Integer>> parents, StringBuilder str) {
            if (node != root && node.getValue().intValue() == 0) {
                return false;
            }
            
            for (int i = 1; i < parents.size(); i++) {
                str.append(parents.get(i).getKey()).append('.');
            }
            str.append(node.getKey());
            
            str.append("=");
            if (parents.size() == 0) {
                str.append(node.getValue());
            } else {
                float rate = node.getValue() * 100F / parents.get(parents.size() - 1).getValue();
                str.append(String .format("%.2f", rate));
                str.append("%");
            }
            
            str.append(", ");
            return true;
        }

        @Override
        public boolean isEmpty() {
            return root.getValue() == null || root.getValue() == 0;
        }

        @Override
        public void clear() {
            clear(root);
            deep = 0;
        }
        
        private void clear(Node<Object, Integer> node) {
            reset(node);
            
            for (Node<Object, Integer> n : node.getNodes().values()) {
                clear(n);
            }
        }
        
        private void reset(Node<Object, Integer> node) {
            node.setValue(0);
        }
        
        private int increment(Node<Object, Integer> node) {
            int count;
            if (node.getValue() == null) {
                count = 1;
            } else {
                count = node.getValue() + 1;
            }
            node.setValue(count);
            return count;
        }
    
        public void add(Object ...es) {
            if (es.length > deep) {
                deep = es.length;
            }
            
            increment(root);
            
            Node<Object, Integer> curr = root;
            Node<Object, Integer> next = root;
            
            for (Object key : es) {
                next = curr.getNodes().get(key);
                if (next == null) {
                    next = new Node<Object, Integer>();
                    next.setKey(key);
                    curr.getNodes().put(key, next);
                }
                increment(next);
                curr = next;
            }
        }
    }
    
    private Result result;

    public TagCounter(String name) {
        super(name);
        result = new Result();
    }

    @Override
    public void record(Object... es) {
        result.add(es);
    }

    @Override
    public Result getResult() {
        return result;
    }

    @Override
    public void clearResult() {
        result.clear();
    }
}
