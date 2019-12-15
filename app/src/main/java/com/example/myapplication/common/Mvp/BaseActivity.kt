package com.example.myapplication.common.Mvp

import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<out P:BasePrester<BaseActivity<P>>>:AppCompatActivity(),IMvpView<P> {
    override val prestener: P

    init {
        prestener=createPresenter()
        prestener.view=this
    }
    private fun createPresenter(): P {
        sequence {
            var thisClass: Class<*> = this@BaseActivity.javaClass
            while (true) {
                yield(thisClass.genericSuperclass)
                thisClass = thisClass.superclass ?: break
            }
        }.filter {
            it is ParameterizedType
        }.flatMap {
            (it as ParameterizedType).actualTypeArguments.asSequence()
        }.first {
            it is Class<*> && IPrestener::class.java.isAssignableFrom(it)
        }.let {
            return (it as Class<P>).newInstance()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        prestener.onDestory()
    }

    override fun onStart() {
        super.onStart()
        prestener.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        prestener.OnSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        prestener.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prestener.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        prestener.onResume()
    }

    override fun onPause() {
        super.onPause()
        prestener.onPause()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        onviewStateRestored(savedInstanceState)
        prestener.onviewStateRestored(savedInstanceState)
    }

    override fun onviewStateRestored(saveInstanceState: Bundle?) {

    }
}
