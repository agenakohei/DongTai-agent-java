package io.dongtai.iast.core.bytecode.enhance.plugin.service.jdbc;

import io.dongtai.iast.core.bytecode.enhance.IastContext;
import io.dongtai.iast.core.bytecode.enhance.plugin.AbstractClassVisitor;
import io.dongtai.log.DongTaiLog;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class MysqlHostInfoAdapter extends AbstractClassVisitor {
    public MysqlHostInfoAdapter(ClassVisitor classVisitor, IastContext context) {
        super(classVisitor, context);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        if ("exposeAsProperties".equals(name)) {
            if (DongTaiLog.isDebugEnabled()) {
                DongTaiLog.debug("Adding MySQL jdbc tracking for type {}.{}", context.getClassName(), name);
            }

            mv = new MysqlHostInfoPropertiesAdviceAdapter(mv, access, name, desc);
            setTransformed();
        }
        return mv;
    }
}