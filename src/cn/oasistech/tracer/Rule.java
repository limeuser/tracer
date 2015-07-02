package cn.oasistech.tracer;


import cn.oasistech.modules.hbase.SourceTypeEnum;
import cn.oasistech.rtb.bid.funnel.FunnelResponse.FunneledType;

public class Rule {
    public final static String TracerName = "rule";
    public final static String FunnelTagTracerName = "funnel";
    public final static String BidTagTracerName = "bid";
    
    public final static Tracer<Event> eventTracer = new Tracer<Event>(TracerName);
    public final static TagTracer funnelTagTracer = new TagTracer(FunnelTagTracerName);
    public final static TagTracer noRecommendTracer = new TagTracer("norecommend");
    public final static TagTracer bidTagTracer = new TagTracer(BidTagTracerName);
    public final static TagTracer priceTagTracer = new TagTracer("price");
    public final static TagTracer planTagTracer = new TagTracer("plan");
    
    public final static String RuleBid = "投标请求监控";
    public final static String Price = "price";
    
    public final static String BidTime = "bidtime";
    
    public final static Span[] redisTimeEvent = SpanUtil.createSpanByDiffStep(0, 50, 1, 1, 2, 2, 2, 2, 2);
    public final static Span[] priceEvent = SpanUtil.createSpanByDiffStep(0, 210, 2,2,2,2,2,5,5,20,20,100);
    // 定义tracer
    static {
        Statistician<Event> bidStat = new Counter<Event>(RuleBid)
        .setRecordElements(BidErrorEvent.values());
        
        //Statistician<Event> bidTimeSpanCounter = new Counter<Event>("redis-time")
        //.setRecordElements(redisTimeEvent);
        
        Statistician<Event> noRecommendCounter = new Counter<Event>("norecommend")
                .setRecordElements(NoRecommend.values());
        
        eventTracer
        .addStatistician(bidStat)
        //.addStatistician(bidTimeSpanCounter)
        //.addStatistician(noRecommendCounter)
        .report(5 * 60);
        
        funnelTagTracer.addStatistician(new TagCounter("弃标原因"))
        .report(5 * 60);
        
        //noRecommendTracer.addStatistician(new TagCounter("norecommend-funnel"))
        //.report(5 * 60);
        
        bidTagTracer.addStatistician(new TagCounter("投标分析"))
        .report(5 * 60);
        
        priceTagTracer.addStatistician(new TagCounter("出价分布"))
        .report(5 * 60);
        
        planTagTracer.addStatistician(new TagCounter("计划过滤"))
        .report(60);
    }
    
    public static void traceEvent(Event e) {
        eventTracer.trace(e);
    }
    
    public static void traceCrowd(int currCrowd, int expectedCrowd, Event e) {
        if (currCrowd == expectedCrowd) {
            traceEvent(e);
        }
    }
    
    public static void traceVip(int crowd, Event e) {
        if (crowd == 161) {
            traceEvent(e);
        }
    }

    public static void traceMarket(int currMarket, int expectedMarket, Event e) {
        if (currMarket == expectedMarket) {
            eventTracer.trace(e);
        }
    }
    
    public static void traceBaidu(int market, Event e) {
        traceMarket(market, SourceTypeEnum.BAIDU.getType(), e);
    }
    
    public enum BidTime implements Comparable<BidTime> {
        Start,
        ReadUserFromRedis,
        Filter,
        Funnel;
    }
    
    public enum NoRecommend implements Event, Comparable<NoRecommend> {
        Start,
        Load,
        Bid;
        
        @Override
        public String getName() {
            return this.name();
        }
    }
    
    public enum BidErrorEvent implements Event, Comparable<BidErrorEvent> {
        Start,
        Balance,
        ProcessTimeout,
        ProcessException,
        WhiteListNoCity,
        WhiteListNoScreen,
        WhiteListNoSize,
        WhiteListNoPid,
        WhiteListNoDomain,
        WhiteListNoMarket,
        WhiteListNotFindCity,
        BidFailed,
        BidSuccess,
        ;
        
        @Override
        public String getName() {
            return this.name();
        }
    }
    
    public enum BidFailedEvent implements Event, Comparable<BidFailedEvent> {
        NotOld,
        NewUser,
        NoRecommend,
        NoGoods,
        Exception,
        FunnelFailed,
        FunnelSuccess,
        Unknown;
        
        @Override
        public String getName() {
        	return this.name();
        }
    }
    
    public enum FunnelEvent implements Event, Comparable<FunnelEvent> {
        Crowd,
        Region,
        Plan,
        Setting,
        BlackList,
        Size,
        WhiteList,
        Price,
        Balance,
        Frequency,
        NoGoods,
        Success,
        // 不存在
        Delta,
        Selection,
        Effect,
        UserType,
        NoRecommend,
        Untreated,
        ;
        
        @Override
        public String getName() {
        	return this.name();
        }
    }

    public static SourceTypeEnum getSourceTypeEnum(int type) {
        for (SourceTypeEnum s : SourceTypeEnum.values()) {
            if (type == s.getType()) {
                return s;
            }
        }
        return SourceTypeEnum.SINA;
    }
    
    public static FunnelEvent getFunnelEvent(FunneledType type) {
        switch (type) {
        case BalanceFunneled:
            return FunnelEvent.Balance;
        case DeltaFunneled:
            return FunnelEvent.Delta;
        case FrequencyFunneled:
            return FunnelEvent.Frequency;
        case CrowdFunneled:  
            return FunnelEvent.Crowd; 
        case RegionFunneled:  
            return FunnelEvent.Region; 
        case PlanFunneled:
            return FunnelEvent.Plan;
        case BlackListFunneled:
            return FunnelEvent.BlackList;
        case WhiteListFunneled:  
            return FunnelEvent.WhiteList; 
        case SettingFunneled:  
            return FunnelEvent.Setting;  
        case UserTypeFunneled:  
            return FunnelEvent.UserType;
        case SelectionFunneled:  
            return FunnelEvent.Selection; 
        case EffectFunneled:  
            return FunnelEvent.Effect;
        case PriceFunneled:  
            return FunnelEvent.Price;
        case NoRecommendFunneled:
            return FunnelEvent.NoRecommend;
        case SizeFunneled:
            return FunnelEvent.Size;
        case Success:
            return FunnelEvent.Success;
        default:
            return FunnelEvent.Untreated;
        }
    }
}
