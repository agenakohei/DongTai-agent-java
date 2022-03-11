package io.dongtai.iast.core.bytecode.enhance.plugin.limiter;


import io.dongtai.iast.common.utils.version.JavaVersionUtils;
import io.dongtai.iast.core.bytecode.enhance.plugin.limiter.breaker.AbstractBreaker;
import io.dongtai.iast.core.bytecode.enhance.plugin.limiter.breaker.DefaultPerformanceBreaker;
import io.dongtai.iast.core.bytecode.enhance.plugin.limiter.breaker.NopBreaker;
import io.dongtai.iast.core.bytecode.enhance.plugin.limiter.breaker.HeavyTrafficBreaker;
import io.dongtai.iast.core.bytecode.enhance.plugin.limiter.impl.HeavyTrafficRateLimiter;
import io.dongtai.iast.core.bytecode.enhance.plugin.limiter.impl.FallbackSwitchFrequencyLimiter;
import io.dongtai.iast.core.utils.threadlocal.RateLimiterThreadLocal;

import java.util.Properties;

/**
 * 限制器管理器
 *
 * @author chenyi
 * @date 2022/3/3
 */
public class LimiterManager {

    private static LimiterManager instance;
    /**
     * 性能断路器
     */
    private final AbstractBreaker performanceBreaker;

    /**
     * 高频流量断路器
     */
    private final AbstractBreaker heavyTrafficBreaker;

    /**
     * hook点高频命中限速器
     */
    private final RateLimiterThreadLocal hookRateLimiter;

    /**
     * 高频流量限速器
     */
    private final HeavyTrafficRateLimiter heavyTrafficRateLimiter;

    /**
     * 降级开关限速器
     */
    private final FallbackSwitchFrequencyLimiter fallbackSwitchFrequencyLimiter;

    public static LimiterManager newInstance(Properties cfg) {
        if (instance == null) {
            instance = new LimiterManager(cfg);
        }
        return instance;
    }

    private LimiterManager(Properties cfg) {
        // 创建断路器实例
        if (JavaVersionUtils.isJava6() || JavaVersionUtils.isJava7()) {
            this.performanceBreaker = NopBreaker.newInstance(cfg);
            this.heavyTrafficBreaker = NopBreaker.newInstance(cfg);
        } else {
            this.performanceBreaker = DefaultPerformanceBreaker.newInstance(cfg);
            this.heavyTrafficBreaker = HeavyTrafficBreaker.newInstance(cfg);
        }
        // 创建限速器实例
        this.hookRateLimiter = new RateLimiterThreadLocal(cfg);
        this.heavyTrafficRateLimiter = new HeavyTrafficRateLimiter(cfg);
        this.fallbackSwitchFrequencyLimiter = new FallbackSwitchFrequencyLimiter(cfg);
    }

    public AbstractBreaker getPerformanceBreaker() {
        return performanceBreaker;
    }

    public RateLimiterThreadLocal getHookRateLimiter() {
        return hookRateLimiter;
    }

    public HeavyTrafficRateLimiter getHeavyTrafficRateLimiter() {
        return heavyTrafficRateLimiter;
    }

    public AbstractBreaker getHeavyTrafficBreaker() {
        return heavyTrafficBreaker;
    }

    public FallbackSwitchFrequencyLimiter getFallbackSwitchFrequencyLimiter() {
        return fallbackSwitchFrequencyLimiter;
    }
}
