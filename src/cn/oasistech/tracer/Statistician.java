package cn.oasistech.tracer;


public abstract class Statistician<E> {
    public abstract class Result {
        public abstract String format();
        public abstract boolean isEmpty();
        public abstract void clear();
        
        @Override
        public String toString() {
            return format();
        }
    }
    
    public abstract void record(E e);
    public abstract Result getResult();
    public abstract void clearResult();

    protected String name;
    protected boolean clearWhenReported;
    
    protected Statistician(String name) {
        this.name = name;
        this.clearWhenReported = true;
    }

    public String getName() {
        return name;
    }
    public boolean isClearWhenReported() {
        return clearWhenReported;
    }
    public Statistician<E> setClearWhenReported(boolean clearWhenReported) {
        this.clearWhenReported = clearWhenReported;
        return this;
    }
}