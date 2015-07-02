package cn.oasistech.tracer;


public abstract class TagStatistician {
    public abstract class Result {
        public abstract String format();
        public abstract String format(int tier);
        public abstract String formatLastTier();
        public abstract boolean isEmpty();
        public abstract void clear();
        
        @Override
        public String toString() {
            return format();
        }
    }
    
    public abstract void record(Object ...es);
    public abstract Result getResult();
    public abstract void clearResult();
    
    private boolean clearWhenReported;
    
    protected TagStatistician(String name) {
        this.name = name;
        this.clearWhenReported = true;
    }
    
    protected String name;
    
    public String getName() {
        return name;
    }
    public boolean isClearWhenReported() {
        return clearWhenReported;
    }
    public TagStatistician setClearWhenReported(boolean clearWhenReported) {
        this.clearWhenReported = clearWhenReported;
        return this;
    }
}