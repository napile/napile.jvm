package org.napile.vm.invoke.impl.nativeimpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author VISTALL
 * @date 23:53/16.02.2012
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface NativeImplement
{
	String className();

	String methodName();

	String[] parameters() default {};
}
