package cn.oasistech.tracer;


import java.util.ArrayList;
import java.util.List;

public class SpanUtil {
    public final static Span[] createSpanByStep(double min, double max, double step) {
        List<Span> spans = new ArrayList<Span>();
        spans.add(new Span(min, max));
        for (double i = min; i < max; i+=step) {
            double start = i;
            double end = i + step;
            if (end >= max) {
                end = max;
            }
            spans.add(new Span(start, end));
        }
        Span[] spanArray = new Span[spans.size()];
        for (int i = 0; i < spanArray.length; i++) {
            spanArray[i] = spans.get(i);
        }
        return spanArray;
    }
    
    public final static Span[] createSpanByDiffStep(double min, double max, double ...steps) {
        double start = 0, end = 0;
        List<Span> spans = new ArrayList<Span>();
        spans.add(new Span(min, max));
        for (int i = 0; i < steps.length; i++) {
            start =  end;
            end = start + steps[i];
            if (end >= max) {
                end = max;
                break;
            }
            spans.add(new Span(start, end));
        }
        
        if (!spans.isEmpty()) {
            Span last = spans.get(spans.size() - 1);
            if (last.getEnd() < max) {
                spans.add(new Span(last.getEnd(), max));
            }
        }
        
        Span[] spanArray = new Span[spans.size()];
        for (int i = 0; i < spanArray.length; i++) {
            spanArray[i] = spans.get(i);
        }
        return spanArray;
    }
    
    public final static Span findSpan(int start, Span[] spans, double value) {
        for (int i = start; i < spans.length; i++) {
            Span span = spans[i];
            if (value >= span.getStart() && value < span.getEnd()) {
                return span;
            }
        }
        
        return spans[spans.length - 1];
    }
}
