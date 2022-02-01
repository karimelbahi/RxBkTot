package com.example.rxbktot

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.rxbktot.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        observer()
//        scheduler()
//        disposable()
//        compositeDisposable()
//        single()
//        completable()
//        maybe()
//        flowable()
//        convertObservablesToOtherOne()
//        coldObservables()
//        coldToHotObservable()
//        subjectAsObservableAndObserver()
//        publishSubjectAsObservableOnly()
//        asyncSubject()
//        behaviourSubject()
//        behaviourSubjectVisualSimulation()
        replySubject()
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

    lateinit var mCompositeDisposable: CompositeDisposable
    private fun compositeDisposable() {
        val observable = Observable.range(1, 1000)
        mCompositeDisposable.add(
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
        )
    }

    override fun onDestroy() {
//        mDisposable.dispose()
        mCompositeDisposable.dispose()
        super.onDestroy()
    }

    private fun single() {
        var single = Single.just(10)
/*        var singleObserver = object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable?) {
                Log.e("karimDebug", "MainActivity, onSubscribe , 21");
            }
            override fun onError(e: Throwable?) {
                Log.e("karimDebug", "MainActivity, onError , 29");
            }
            override fun onSuccess(t: Int?) {
                Log.e("karimDebug", "MainActivity, onSuccess , 76");
            }
        }
        single.subscribe(singleObserver)*/

        // lambda
/*        single.subscribe(
            {
                Log.e("karimDebug", "$it MainActivity, onNext , 25");

            },
            {
                Log.e("karimDebug", "$it MainActivity, onError , 29");

            }
        )*/

        // higher order function
        single.subscribe(::onDateSuccess, ::onDataError)

    }

    private fun onDateSuccess(result: Int) {
        Log.e("karimDebug", "MainActivity, onDateSuccess , 151");
    }

    private fun onDataError(e: Throwable) {
        Log.e("karimDebug", "MainActivity, onDataError , 156");
    }

    private fun completable() {
        val completable = Completable.create { emitter ->
            binding.editText.doOnTextChanged { text, start, before, count ->
                if (text.toString() == "completable") {
                    emitter.onComplete()
                } else {
                    Log.e("karimDebug", "MainActivity, completable , 170");
                }
            }
        }

/*        completable.subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable?) {

            }

            override fun onComplete() {
                Log.e("karimDebug","MainActivity, onComplete , 180");
            }

            override fun onError(e: Throwable?) {

            }
        })*/
/*        completable.subscribe(
            {
                Log.e("karimDebug", "onComplete MainActivity, completable , 173");
            }, { e ->
                Log.e("karimDebug","onError MainActivity, completable , 191");
            }
        )*/
        completable.subscribe(::onCompletableSuccess, ::onCompletableError)
    }

    private fun onCompletableSuccess() {
        Log.e("karimDebug", "MainActivity, onCompletableSuccess , 196");
    }

    private fun onCompletableError(e: Throwable) {
        Log.e("karimDebug", "MainActivity, onCompletableError , 199");
    }

    private fun maybe() {
        val maybe = Maybe.create<String> { emitter ->
            binding.editText.doOnTextChanged { text, start, before, count ->
                when (text.toString()) {
                    "maybe" -> emitter.onSuccess("hi, form maybe")
                    "other" -> emitter.onComplete()
                }
            }
        }
        maybe.subscribe(
            {
                Log.e("karimDebug", "$it MainActivity, maybe , 216");
            },
            {
                Log.e("karimDebug", "MainActivity, maybe , 219");
            },
            {
                Log.e("karimDebug", "MainActivity, maybe , 222");
            }
        )
    }

    private fun flowable() {
        var flowable = Flowable.range(1, 1000)

        flowable.onBackpressureLatest()
            .observeOn(Schedulers.io(), false, 5)
            .subscribe({
                Log.e("karimDebug", "$it MainActivity, flowable , 234");
            }, {
                Log.e("karimDebug", "MainActivity, flowable , 236");
            }, {
                Log.e("karimDebug", "MainActivity, flowable , 238");
            })
    }

    private fun convertObservablesToOtherOne() {
        var observable = Observable.range(1, 1000)

        observable.toFlowable(BackpressureStrategy.MISSING)
            .observeOn(Schedulers.io(), false, 5)
            .subscribe({
                Log.e("karimDebug", "$it MainActivity, flowable , 234");
            }, {
                Log.e("karimDebug", "MainActivity, flowable , 236");
            }, {
                Log.e("karimDebug", "MainActivity, flowable , 238");
            })

    }

    private fun coldObservables() {
        var observable = Observable.interval(1, TimeUnit.SECONDS).take(10)

        observable.subscribe({
            Log.e("karimDebug", "$it MainActivity, flowable , 234");
        }, {
            Log.e("karimDebug", "MainActivity, flowable , 236");
        }, {
            Log.e("karimDebug", "MainActivity, flowable , 238");
        })

        Thread.sleep(4000)

        observable.subscribe({
            Log.e("karimDebug", "$it MainActivity, flowable , 234");
        }, {
            Log.e("karimDebug", "MainActivity, flowable , 236");
        }, {
            Log.e("karimDebug", "MainActivity, flowable , 238");
        })
    }

    private fun coldToHotObservable() {
        var observable = Observable
            .interval(1, TimeUnit.SECONDS)
            .take(10)
            .publish()
        observable.connect()

//        Thread.sleep(1000)

        observable.subscribe({
            Log.e("karimDebug", "$it MainActivity, flowable , 234");
        }, {
            Log.e("karimDebug", "MainActivity, flowable , 236");
        }, {
            Log.e("karimDebug", "MainActivity, flowable , 238");
        })

        Thread.sleep(4000)

        observable.subscribe({
            Log.e("karimDebug", "$it MainActivity, flowable , 234");
        }, {
            Log.e("karimDebug", "MainActivity, flowable , 236");
        }, {
            Log.e("karimDebug", "MainActivity, flowable , 238");
        })
    }

    private fun subjectAsObservableAndObserver() {
        var observable = Observable
            .interval(1, TimeUnit.SECONDS)
            .take(10)

        val subject = PublishSubject.create<Long>()

        observable.subscribe(subject)

        Thread.sleep(3000)

        /**
         * will not start form 0 because it started by the previous subscribe before next one
         * */
        subject.subscribe({
            Log.e("karimDebug", "$it MainActivity, , 234");
        }, {
            Log.e("karimDebug", "MainActivity, , 236");
        }, {
            Log.e("karimDebug", "MainActivity, , 238");
        })

        Thread.sleep(2000)

        /*
        * will continue with first one because it is hot
        * */
        subject.subscribe({
            Log.e("karimDebug", "$it MainActivity,  234");
        }, {
            Log.e("karimDebug", "MainActivity,  236");
        }, {
            Log.e("karimDebug", "MainActivity,  238");
        })

        /*
        * will start from new because it is cold
        * */
        observable.subscribe({
            Log.e("karimDebug", "$it MainActivity,  234");
        }, {
            Log.e("karimDebug", "MainActivity,  236");
        }, {
            Log.e("karimDebug", "MainActivity,  238");
        })

    }

    private fun publishSubjectAsObservableOnly() {

        val subject = PublishSubject.create<Long>()

        /*
        * will be ignored
        * */
        subject.onNext(1)
        subject.onNext(2)

        binding.editText.doOnTextChanged { text, start, before, count ->
            if (text.toString() == "start") {
                subject.subscribe({
                    Log.e("karimDebug", "$it MainActivity, , 234");
                }, {
                    Log.e("karimDebug", "MainActivity, , 236");
                }, {
                    Log.e("karimDebug", "MainActivity, , 238");
                })
            }

            subject.onNext(10)
            subject.onNext(20)
        }
    }

    private fun asyncSubject() {

        var observable = Observable
            .interval(1, TimeUnit.SECONDS)
            .take(3)


        val subject = AsyncSubject.create<Long>()
        observable.subscribe(subject)

//        Thread.sleep(4000)

        subject.subscribe({
            Log.e("karimDebug", "$it MainActivity, , 234");
        }, {
            Log.e("karimDebug", "MainActivity, , 236");
        }, {
            Log.e("karimDebug", "MainActivity, , 238");
        })

        subject.onNext(10)
        subject.onNext(20)

    }

    private fun behaviourSubject() {

        var observable = Observable
            .interval(500, TimeUnit.MILLISECONDS)
            .take(10)

        val subject = BehaviorSubject.create<Long>()

        observable.subscribe(subject)

        Thread.sleep(1200)

        subject.subscribe({
            Log.e("karimDebug", "$it MainActivity, , 234");
        }, {
            Log.e("karimDebug", "MainActivity, , 236");
        }, {
            Log.e("karimDebug", "MainActivity, , 238");
        })

    }

    private fun behaviourSubjectVisualSimulation() {

        val subject = BehaviorSubject.create<Long>()
        /*
         * will be ignored
         * */
        subject.onNext(1)
        subject.onNext(2)
        subject.subscribe({
            Log.e("karimDebug", "$it MainActivity, , 234");
        }, {
            Log.e("karimDebug", "MainActivity, , 236");
        }, {
            Log.e("karimDebug", "MainActivity, , 238");
        })

        subject.onNext(10)
        subject.onNext(20)

    }

    private fun replySubject() {

        var observable = Observable
            .interval(500, TimeUnit.MILLISECONDS)
            .take(10)

        val subject = ReplaySubject.create<Long>()

        observable.subscribe(subject)

        Thread.sleep(4000)

        subject.subscribe({
            Log.e("karimDebug", "$it MainActivity, , 234");
        }, {
            Log.e("karimDebug", "MainActivity, , 236");
        }, {
            Log.e("karimDebug", "MainActivity, , 238");
        })

    }

}