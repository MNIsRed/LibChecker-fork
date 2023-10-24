package com.absinthe.libchecker.ui.base

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * 反射实现声明ViewBinding泛型的视图自动绑定layoutInflater并注入binding
 * sealed保证只有在com.absinthe.libchecker.ui.base文件夹下声明的类能继承它。
 * internal保证其他模块无法访问这个类。
 */
internal sealed interface IBinding<VB : ViewBinding> {
  //大概是考虑到Fragment这种需要解绑的情况，所以才不直接在这里完成注入？
  val binding: VB

  fun <T : ViewBinding> inflateBinding(inflater: LayoutInflater): T {
    var method: Method?
    //以MainActivity举例，此时clazz是MainActivity<ActivityMainBinding>
    var clazz: Class<*> = javaClass
    while (clazz.superclass != null) {
      method = clazz.filterBindingMethod()
      if (method == null) {
        clazz = clazz.superclass
      } else {
        @Suppress("UNCHECKED_CAST")

        return method.invoke(null, inflater) as T
      }
    }
    error("No Binding type argument found.")
  }

  private fun Class<*>.filterBindingMethod(): Method? {
    //获取泛型信息
    return (genericSuperclass as? ParameterizedType)?.actualTypeArguments
      //Array转成Sequence，大概是因为操作比较多，担心内存？
      ?.asSequence()
      ?.filterIsInstance<Class<*>>()
      ?.firstOrNull { it.simpleName.endsWith("Binding") }
      ?.getDeclaredMethod("inflate", LayoutInflater::class.java)
      ?.also { it.isAccessible = true }
  }
}
