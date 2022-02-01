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
import io.reactivex.rxjava3.kotlin.toObservable
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

        /*
         * operators
         * */
        fromIterable()
        fromArray()


        /*
        * observables
        * */
        observer()
        scheduler()
        disposable()
        compositeDisposable()
        single()
        completable()
        maybe()
        flowable()
        convertObservablesToAnotherOne()
        coldObservables()
        coldToHotObservable()
        subjectAsObservableAndObserver()
        publishSubjectAsObservableOnly()
        asyncSubject()
        behaviourSubject()
        behaviourSubjectVisualSimulation()
        replySubject()
        rxKotlin()

    }


    private fun fromIterable() {
        val list = listOf(1, 2, 3, 4, 5, 67, 8, 9)
        val observable = Observable.fromIterable(list)
        observable.subscribe({
            Log.d("infoLog", "$it fromIterable , 59");
        }, {
            Log.d("infoLog", "fromIterable , 61");
        })

    }

    private fun fromArray() {
        val observable = Observable.fromArray(1, 2, 3, 4, 5, 67, 8, 9)
        observable.subscribe({
            Log.d("infoLog", "$it fromArray , 71");
        }, {
            Log.e("karimDebug", "MainActivity, fromArray , 73");
        })

    }


   // observables
    private fun observer() {
        var firstObservable =
            Observable.interval(1, TimeUnit.MILLISECONDS).take(10).map { it * 100 }
        var observable = Observable.interval(2, TimeUnit.MILLISECONDS).take(100).map { it * 2 }
            .concatWith(firstObservable)
/*        var observer = object : Observer<Int> {
            override fun onSubscribe(d: Disposable?) {
                Log.d("infoLog","onSubscribe , 57");
            }

            override fun onNext(t: Int?) {
                Log.d("infoLog","onNext , 61");
            }

            override fun onError(e: Throwable?) {
                Log.d("infoLog","onError , 65");
            }

            override fun onComplete() {
                Log.d("infoLog","onComplete , 69");
            }

        }
        observable.subscribe(observer)*/
        observable.subscribe(
            {
                Log.d("infoLog", "$it MainActivity, onNext, 76");

            },
            {
                Log.d("infoLog", "$it MainActivity, onError, 80");

            },
            {
                Log.d("infoLog", "MainActivity, onComplete, 84");
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
                    Log.d("infoLog", "MainActivity, schedule, 96");
                },
                { e ->
                    Log.d("infoLog", "MainActivity, schedule, 99");
                })
    }

    private lateinit var mDisposable: Disposable
    private fun disposable() {
        val observable = Observable.range(1, 1000)
        mDisposable = observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { s ->
                    Log.d("infoLog", "MainActivity, schedule, 110");
                },
                { e ->
                    Log.d("infoLog", "MainActivity, schedule, 113");
                })
    }

    private lateinit var mCompositeDisposable: CompositeDisposable
    private fun compositeDisposable() {
        mCompositeDisposable = CompositeDisposable()
        val observable = Observable.range(1, 1000)
        mCompositeDisposable.add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { s ->
                        Log.d("infoLog", "MainActivity, compositeDisposable, 127");
                    },
                    { e ->
                        Log.d("infoLog", "MainActivity, schedule, 130");
                    })
        )
    }

    override fun onDestroy() {
//        mDisposable.dispose()
//        mCompositeDisposable.dispose()
        super.onDestroy()
    }

    private fun single() {
        var single = Single.just(10)
/*        var singleObserver = object : SingleObserver<Int> {
            override fun onSubscribe(d: Disposable?) {
                Log.d("infoLog","onSubscribe , 145");
            }
            override fun onError(e: Throwable?) {
                Log.d("infoLog","onError , 148");
            }
            override fun onSuccess(t: Int?) {
                Log.d("infoLog","onSuccess , 151");
            }
        }
        single.subscribe(singleObserver)*/

        // lambda
/*        single.subscribe(
            {
                Log.d("infoLog","$it single , 159");

            },
            {
                Log.d("infoLog","$it single , 163");

            }
        )*/

        // higher order function
        single.subscribe(::onDateSuccess, ::onDataError)

    }

    private fun onDateSuccess(result: Int) {
        Log.d("infoLog", "$result onDateSuccess , 174");
    }

    private fun onDataError(e: Throwable) {
        Log.d("infoLog", "onDataError , 178");
    }

    private fun completable() {
        val completable = Completable.create { emitter ->
            binding.editText.doOnTextChanged { text, start, before, count ->
                if (text.toString() == "completable") {
                    emitter.onComplete()
                } else {
                    Log.d("infoLog", "completable , 187");
                }
            }
        }

/*        completable.subscribe(object : CompletableObserver {
            override fun onSubscribe(d: Disposable?) {
                Log.d("infoLog", "onSubscribe , 194");
            }

            override fun onComplete() {
                Log.d("infoLog", "onComplete , 198");
            }

            override fun onError(e: Throwable?) {
                Log.d("infoLog", "onError , 202");
            }
        })*/
/*        completable.subscribe(
            {
                Log.d("infoLog","completable , 207");
            }, { e ->
                Log.d("infoLog","completable , 209");
            }
        )*/
        completable.subscribe(::onCompletableSuccess, ::onCompletableError)
    }

    private fun onCompletableSuccess() {
        Log.d("infoLog", "onCompletableSuccess , 216");
    }

    private fun onCompletableError(e: Throwable) {
        Log.d("infoLog", "onCompletableError , 220");
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
                Log.d("infoLog", "$it MainActivity, maybe");
            },
            {
                Log.d("infoLog", "MainActivity, maybe");
            },
            {
                Log.d("infoLog", "MainActivity, maybe");
            }
        )
    }

    private fun flowable() {
        var flowable = Flowable.range(1, 1000)

        flowable.onBackpressureLatest()
            .observeOn(Schedulers.io(), false, 5)
            .subscribe({
                Log.d("infoLog", "$it  flowable , 251");
            }, {
                Log.d("infoLog", "flowable , 253");
            }, {
            }
            )
    }

    private fun convertObservablesToAnotherOne() {
        var observable = Observable.range(1, 1000)

        observable.toFlowable(BackpressureStrategy.MISSING)
            .observeOn(Schedulers.io(), false, 5)
            .subscribe({
                Log.d("infoLog", "$it convertObservablesToAnotherOne , 265");
            }, {
                Log.d("infoLog", "convertObservablesToAnotherOne , 267");
            }, {
                Log.d("infoLog", "convertObservablesToAnotherOne , 270");
            })

    }

    private fun coldObservables() {
        var observable = Observable.interval(1, TimeUnit.SECONDS).take(10)

        observable.subscribe({
            Log.d("infoLog", "$it coldObservables , 278");
            Log.d("infoLog", "$it MainActivity, flowable");
        }, {
            Log.d("infoLog", "coldObservables , 281");
        }, {
            Log.d("infoLog", "coldObservables , 284");
        })

        Thread.sleep(4000)

        observable.subscribe({
            Log.d("infoLog", "$it coldObservables , 289");
        }, {
            Log.d("infoLog", "coldObservables , 291");
        }, {
            Log.d("infoLog", "coldObservables , 293");
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
            Log.d("infoLog", "$it coldToHotObservable , 307");
        }, {
            Log.d("infoLog", "coldToHotObservable , 309");
        }, {
            Log.d("infoLog", "coldToHotObservable , 311");
        })

        Thread.sleep(4000)

        observable.subscribe({
            Log.d("infoLog", "$it coldToHotObservable , 317");
        }, {
            Log.d("infoLog", "coldToHotObservable , 319");
        }, {
            Log.d("infoLog", "coldToHotObservable , 321");
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
            Log.d("infoLog", "$it subjectAsObservableAndObserver , 340");
        }, {
            Log.d("infoLog", "subjectAsObservableAndObserver , 342");
        }, {
            Log.d("infoLog", "subjectAsObservableAndObserver , 344");
        })

        Thread.sleep(2000)

        /*
        * will continue with first one because it is hot
        * */
        subject.subscribe({
            Log.d("infoLog", "$it subjectAsObservableAndObserver , 353");
        }, {
            Log.d("infoLog", "subjectAsObservableAndObserver , 355");
        }, {
            Log.d("infoLog", "subjectAsObservableAndObserver , 357");
        })

        /*
        * will start from new because it is cold
        * */
        observable.subscribe({
            Log.d("infoLog", "$it subjectAsObservableAndObserver , 364");
        }, {
            Log.d("infoLog", "subjectAsObservableAndObserver , 366");
        }, {
            Log.d("infoLog", "subjectAsObservableAndObserver , 368");
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
                    Log.d("infoLog", "$it publishSubjectAsObservableOnly , 386");
                }, {
                }, {
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
            Log.d("infoLog", "$it asyncSubject , 410");
        }, {
            Log.d("infoLog", "asyncSubject , 412");
        }, {
            Log.d("infoLog", "asyncSubject , 414");
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
            Log.d("infoLog", "$it behaviourSubject , 435");
        }, {
            Log.d("infoLog", "behaviourSubject , 437");
        }, {
            Log.d("infoLog", "behaviourSubject , 439");
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
            Log.d("infoLog", "$it behaviourSubjectVisualSimulation , 453");
        }, {
            Log.d("infoLog", "behaviourSubjectVisualSimulation , 455");
        }, {
            Log.d("infoLog", "behaviourSubjectVisualSimulation , 457");
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
            Log.d("infoLog", "$it replySubject , 478");
        }, {
            Log.d("infoLog", "replySubject , 480");
        }, {
            Log.d("infoLog", "replySubject , 482");
        })

    }

    private fun rxKotlin() {
        val list = listOf("hello", "from", "rxkotlin")

        /*
         *rx java
         */
        // val observable=Observable.fromIterable(list)

        /*
        * rx kotlin
        * doc sample https://github.com/ReactiveX/RxKotlin
        * */list.toObservable().subscribe({
            Log.d("infoLog", "$it rxKotlin , 499");
        }, {
            Log.d("infoLog", "rxKotlin , 501");
        })

    }


}