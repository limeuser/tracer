package cn.oasistech.tracer;


public class Span implements Event, Comparable<Span>{
    private double start;
    private double end;
    
    public Span(double start, double end) {
        this.start = start;
        this.end = end;
    }
    
    public double getStart() {
        return start;
    }
    public void setStart(double start) {
        this.start = start;
    }
    public double getEnd() {
        return end;
    }
    public void setEnd(double end) {
        this.end = end;
    }

    @Override
    public String getName() {
        return new StringBuilder()
        .append('[')
        .append(String.format("%.3f", start))
        .append('~')
        .append(String.format("%.3f", end))
        .append(']')
        .toString();
    }
    
    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(Span that) {
        double diff = this.start - that.getStart();
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        }
        return 0;
    }
}
