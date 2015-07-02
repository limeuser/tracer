package cn.oasistech.tracer;


import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Counter<E> extends Statistician<E> {
    public class Result extends Statistician<E>.Result {
        private ConcurrentHashMap<E, Integer> count;

        public Result() {
            count = new ConcurrentHashMap<E, Integer>();
        }
        
        @Override
        public String format() {
            int total = count.get(baseElement);
            boolean notEmpty = false;
            StringBuilder str = new StringBuilder();
            str.append(getName() + ": ");

            if (recordElements != null) {
                for (E e : recordElements) {
                    if (formatElement(e, count.get(e), total, str)) {
                    	notEmpty = true;
                    }
                }
            } else {
                for (Entry<E, Integer> entry : count.entrySet()) {
                    if (formatElement(entry.getKey(), entry.getValue(), total, str)) {
                    	notEmpty = true;
                    }
                }
            }
            
            if (notEmpty) {
                str.setLength(str.length() - 2);
            }
            
            return str.toString();
        }
        
        private boolean formatElement(E e, Integer c, int total, StringBuilder str) {
            if (!e.equals(baseElement) && c.intValue() == 0) {
                return false;
            }
            
            str.append(e);
            str.append('=');
            if (e.equals(baseElement)) {
                str.append(c);
            } else {
                float rate = 0F;
                if (total != 0) {
                    rate = c * 100F / total;
                }
                str.append(String .format("%.2f", rate));
                str.append('%');
            }
            str.append(", ");
            return true;
        }

        @Override
        public boolean isEmpty() {
            return count.isEmpty();
        }

        @Override
        public void clear() {
            for (E e : count.keySet()) {
            	count.put(e, 0);
            }
        }
    
        public void add(E e) {
            Integer c = count.get(e);
            if (c == null) {
                count.put(e, 1);
            } else {
                count.put(e, c + 1);
            }
        }
        
        public ConcurrentHashMap<E, Integer> getCount() {
            return count;
        }
    }
    
    private Result result;
    private E baseElement;  
    private E[] recordElements;
    
    public Counter(String name) {
        super(name);
        result = new Result();
    }
    
    public Counter<E> setRecordElements(E[] es) {
        this.recordElements = es;
        for (E e : es) {
            result.count.put(e, 0);
        }
        
        // 默认以第一个元素作为基础元素计算比例
        baseElement = es[0];
        
        return this;
    }
    
    public Counter<E> setBaseElement(E e) {
        this.baseElement = e;
        return this;
    }
    
    @Override
    public void record(E e) {
        if (recordElements == null || result.count.get(e) != null) {
            result.add(e);
        }
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
