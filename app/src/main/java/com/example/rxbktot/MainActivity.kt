package com.example.rxbktot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.widget.doOnTextChanged
import com.example.rxbktot.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        observer()
//        scheduler()
        disposable()
    }

    private fun observer() {
        var firstObservable =
            Observable.interval(1, TimeUnit.MILLISECONDS).take(10).map { it * 100 }
        var observable = Observable.interval(2, TimeUnit.MILLISECONDS).take(100).map { it * 2 }
            .concatWith(firstObservable)
/*        var observer = object : Observer<Int> {
            override fun onSubscribe(d: Disposable?) {
                Log.e("karimDebug", "MainActivity, onSubscribe , 21");
            }

            override fun onNext(t: Int?) {
                Log.e("karimDebug", "MainActivity, onNext , 25");
            }

            override fun onError(e: Throwable?) {
                Log.e("karimDebug", "MainActivity, onError , 29");
            }

            override fun onComplete() {
                Log.e("karimDebug", "MainActivity, onComplete , 33");
            }

        }*/
//        observable.subscribe(observer)
        observable.subscribe(
            {
                Log.e("karimDebug", "$it MainActivity, onNext , 25");

            },
            {
                Log.e("karimDebug", "$it MainActivity, onError , 29");

            },
            {
                Log.e("karimDebug", "MainActivity, onComplete , 33");
            }
        )
    }

    private fun scheduler() {
        val observable = Observable.range(1, 1000)
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { s ->
                    Log.e("karimDebug", "MainActivity, scheduler , 70");
                },
                { e ->
                    Log.e("karimDebug", "MainActivity, scheduler , 73");
                })
    }

    lateinit var mDisposable: Disposable
    private fun disposable() {
        val observable = Observable.range(1, 1000)
        mDisposable = observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { s ->
                    Log.e("karimDebug", "MainActivity, scheduler , 70");
                },
                { e ->
                    Log.e("karimDebug", "MainActivity, scheduler , 73");
                })
    }


}