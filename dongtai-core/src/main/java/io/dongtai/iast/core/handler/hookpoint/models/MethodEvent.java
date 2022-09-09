package io.dongtai.iast.core.handler.hookpoint.models;

import io.dongtai.iast.core.handler.hookpoint.vulscan.taintrange.TaintRanges;
import io.dongtai.iast.core.handler.hookpoint.vulscan.taintrange.TaintRangesBuilder;
import io.dongtai.iast.core.utils.PropertyUtils;
import io.dongtai.log.DongTaiLog;
import org.json.JSONObject;

import java.util.*;

/**
 * 方法事件
 *
 * @author dongzhiyong@huoxian.cn
 */
public class MethodEvent {

    /**
     * 调用过程ID
     */
    private final int processId;

    public int getInvokeId() {
        return invokeId;
    }

    /**
     * 调用ID
     */
    private int invokeId;

    private final boolean isStatic;

    public String getOriginClassName() {
        return originClassName;
    }

    private final String originClassName;

    public String getMatchClassName() {
        return matchClassName;
    }

    /**
     * 获取触发调用事件的类名称
     */
    private final String matchClassName;

    public String getMethodName() {
        return methodName;
    }

    /**
     * 获取触发调用事件的方法名称
     */
    private final String methodName;

    public String getMethodDesc() {
        return methodDesc;
    }

    /**
     * 获取触发调用事件的方法签名
     */
    private final String methodDesc;

    /**
     * 获取触发调用事件的对象
     */
    public final Object object;

    /**
     * 获取触发调用事件的方法参数
     */
    public final Object[] argumentArray;

    /**
     * 构造方法返回值
     */
    public Object returnValue;

    /**
     * 方法的污点进入值
     */
    public Object inValue;

    public String inValueString = "";

    public Set<Integer> getSourceHashes() {
        return sourceHashes;
    }

    private final Set<Integer> sourceHashes = new HashSet<Integer>();


    public Set<Integer> getSourceHashForRpc() {
        return sourceHashForRpc;
    }

    private final Set<Integer> sourceHashForRpc = new HashSet<Integer>();

    public Set<Integer> getTargetHashes() {
        return targetHashes;
    }

    private final Set<Integer> targetHashes = new HashSet<Integer>();

    public Set<Integer> getTargetHashForRpc() {
        return targetHashForRpc;
    }

    private final Set<Integer> targetHashForRpc = new HashSet<Integer>();

    public void addSourceHash(int hashcode) {
        this.sourceHashes.add(hashcode);
    }

    public void addSourceHashForRpc(int hash) {
        this.sourceHashForRpc.add(hash);
    }

    public void addTargetHash(int hashCode) {
        this.targetHashes.add(hashCode);
    }

    public void addTargetHashForRpc(int hash) {
        this.targetHashForRpc.add(hash);
    }

    /**
     * 方法的传出值
     */
    public Object outValue;

    public String outValueString = "";

    /**
     * 方法的签名
     */
    public String signature;

    /**
     * 构建事件的子事件
     */
    public List<MethodEvent> subEvent;

    /**
     * 构造方法的离开标志
     */
    public boolean leave;

    public boolean isSource() {
        return source;
    }

    /**
     * 构造方法的类型标志
     */
    public boolean source;

    /**
     * 是否是 辅助方法
     */
    public boolean auxiliary;

    /**
     * 多条件匹配时的条件
     */
    public String condition;

    /**
     * 方法的调用栈
     */
    public StackTraceElement[] callStacks;

    private StackTraceElement callStack;

    /**
     * 方法的框架
     */
    public String framework;

    /**
     * 当前服务的 traceId
     */
    public String traceId;

    /**
     * 当前服务的名称
     */
    public String serviceName;

    /**
     * 当前服务使用的rpc框架
     */
    public String plugin;

    public Boolean getProjectPropagatorClose() {
        return projectPropagatorClose;
    }

    public void setProjectPropagatorClose(Boolean projectPropagatorClose) {
        this.projectPropagatorClose = projectPropagatorClose;
    }

    public Boolean projectPropagatorClose = false;

    public List<MethodEventTargetRange> targetRanges = new ArrayList<MethodEventTargetRange>();

    public List<MethodEventSourceType> sourceTypes;

    public static class MethodEventSourceType {
        private final Integer hash;
        private final String type;

        public MethodEventSourceType(Integer hash, String type) {
            this.hash = hash;
            this.type = type;
        }

        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("hash", this.hash);
            json.put("type", this.type);
            return json;
        }
    }

    public static class MethodEventTargetRange {
        private final Integer hash;
        private final String value;
        private final TaintRanges ranges;

        public MethodEventTargetRange(Integer hash, String value, TaintRanges ranges) {
            this.hash = hash;
            this.value = value;
            this.ranges = ranges;
        }

        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("hash", this.hash);
            json.put("value", this.value);
            json.put("ranges", this.ranges.toJson());
            return json;
        }
    }

    /**
     * 构造调用事件
     *
     * @param processId      调用者ID
     * @param invokeId       方法调用ID
     * @param matchClassName 方法所在类的名称
     * @param methodName     方法名称
     * @param methodDesc     方法描述符
     * @param object         方法对应的实例化对象
     * @param argumentArray  方法参数
     * @param isStatic       方法是否为静态方法，true-静态方法；false-实例方法
     */
    public MethodEvent(final int processId,
                       final int invokeId,
                       final String originClassName,
                       final String matchClassName,
                       final String methodName,
                       final String methodDesc,
                       final String signature,
                       final Object object,
                       final Object[] argumentArray,
                       final Object returnValue,
                       final String framework,
                       final boolean isStatic,
                       final StackTraceElement[] callStack) {
        this.processId = processId;
        this.invokeId = invokeId;
        this.matchClassName = matchClassName;
        this.originClassName = originClassName;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.signature = signature;
        this.object = object;
        this.argumentArray = argumentArray;
        this.returnValue = returnValue;
        this.leave = false;
        this.source = false;
        this.auxiliary = false;
        this.condition = "";
        this.isStatic = isStatic;
        this.callStacks = callStack;
        this.framework = framework;
    }

    /**
     * 改变方法入参
     *
     * @param index       方法入参编号(从0开始)
     * @param changeValue 改变的值
     * @return this
     * @since {@code sandbox-api:1.0.10}
     */
    public MethodEvent changeParameter(final int index,
                                       final Object changeValue) {
        argumentArray[index] = changeValue;
        return this;
    }

    public void setCallStacks(StackTraceElement[] callStacks) {
        this.callStacks = callStacks;
        this.setCallStack(callStacks[0]);
    }

    public Object getObject() {
        return this.object;
    }


    public void setInvokeId(int invokeId) {
        this.invokeId = invokeId;
    }

    public String obj2String(Object value) {
        StringBuilder sb = new StringBuilder();
        if (null == value) {
            return "";
        }
        try {
            if (value instanceof byte[]) {
                return TaintRangesBuilder.trimRight((byte[]) value);
            } else if (value instanceof char[]) {
                return TaintRangesBuilder.trimRight((char[]) value);
            } else if (value.getClass().isArray() && !value.getClass().getComponentType().isPrimitive()) {
                // 判断是否是基本类型的数组，基本类型的数组无法类型转换为Object[]，导致java.lang.ClassCastException异常
                Object[] taints = (Object[]) value;
                for (Object taint : taints) {
                    if (taint != null) {
                        if (taint.getClass().isArray() && !taint.getClass().getComponentType().isPrimitive()) {
                            Object[] subTaints = (Object[]) taint;
                            for (Object subTaint : subTaints) {
                                sb.append(subTaint.toString()).append(" ");
                            }
                        } else {
                            sb.append(taint.toString()).append(" ");
                        }
                    }
                }
            } else {
                sb.append(value.toString());
            }
        } catch (Exception e) {
            sb.append("CustomObjectValue");
        }
        return sb.toString().trim();
    }

    public boolean objIsReference(Object value) {
        if (null == value) {
            return false;
        }
        try {
            String valueToString = value.toString();
            String classObjectToString = value.getClass().getName() + "@" + Integer.toHexString(hashCode());
            return valueToString.contains(classObjectToString);
        } catch (Exception e) {
            DongTaiLog.error(e);
            return false;
        }
    }

    @Override
    public String toString() {
        return "MethodEvent{" +
                "processId=" + processId +
                ", invokeId=" + invokeId +
                ", isStatic=" + isStatic +
                ", originClassName='" + originClassName + '\'' +
                ", matchClassName='" + matchClassName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodDesc='" + methodDesc + '\'' +
                ", object=" + object +
                ", argumentArray=" + Arrays.toString(argumentArray) +
                ", returnValue=" + returnValue +
                ", inValue=" + inValue +
                ", sourceHashes=" + sourceHashes +
                ", sourceHashForRpc=" + sourceHashForRpc +
                ", targetHashes=" + targetHashes +
                ", targetHashForRpc=" + targetHashForRpc +
                ", outValue=" + outValue +
                ", signature='" + signature + '\'' +
                ", subEvent=" + subEvent +
                ", leave=" + leave +
                ", source=" + source +
                ", auxiliary=" + auxiliary +
                ", condition='" + condition + '\'' +
                ", callStacks=" + Arrays.toString(callStacks) +
                ", callStack=" + callStack +
                ", framework='" + framework + '\'' +
                ", traceId='" + traceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", plugin='" + plugin + '\'' +
                ", projectPropagatorClose=" + projectPropagatorClose +
                '}';
    }

    public String getCallerClass() {
        return callStack.getClassName();
    }

    public String getCallerMethod() {
        return callStack.getMethodName();
    }

    public int getCallerLine() {
        return callStack.getLineNumber();
    }

    public void setCallStack(StackTraceElement callStack) {
        this.callStack = callStack;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public void setInValue(Object inValue) {
        PropertyUtils properties = PropertyUtils.getInstance();
        this.inValue = inValue;
        this.inValueString = properties.isLocal() ? obj2String(inValue) : "";
    }

    public void setOutValue(Object outValue) {
        PropertyUtils properties = PropertyUtils.getInstance();
        this.outValue = outValue;
        this.outValueString = properties.isLocal() ? obj2String(outValue) : "";
    }
}
