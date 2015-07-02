package cn.oasistech.tracer;


import java.util.ArrayList;
import java.util.List;

public class TimeTracer {
    public class TimeTag {
        public TimeTag(Object tag, long time) {
            this.tag = tag;
            this.time = time;
        }
        public long time;
        public Object tag;
        
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append(tag).append(':').append(time);
            return str.toString();
        }
    }
    
    private String name;
    public TimeTracer(String name) {
        this.name = name;
    }
    
    private List<TimeTag> timeTags = new ArrayList<TimeTag>();

    public void trace(Object tag) {
        timeTags.add(new TimeTag(tag, System.currentTimeMillis()));
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(name).append(": ");
        str.append("total=").append(getTotalTime()).append(", ");
        
        TimeTag pre = null;
        if (false == timeTags.isEmpty()) {
            pre = timeTags.get(0);
        }
        
        for (TimeTag tt : timeTags) {
            str.append(tt.tag).append('=').append(tt.time - pre.time).append(", ");
            pre = tt;
        }
        
        str.setLength(str.length() - ", ".length());
        
        str.append("\r\n");
        return str.toString();
    }
    
    public long getTotalTime() {
        if (timeTags.isEmpty()) {
            return 0;
        }
        return timeTags.get(timeTags.size() - 1).time - timeTags.get(0).time;
    }
}
