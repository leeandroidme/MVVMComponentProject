package com.newland.core.utils

import java.util.concurrent.*

/**
 * @author: leellun
 * @data: 18/6/2021.
 *
 */
object ThreadPool {
    private val executor: Executor = Executors.newCachedThreadPool()
    private val scheduledExecutorService =
        Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors())
    private val executorRunnables = ConcurrentHashMap<Runnable, ScheduledFuture<*>>()

    fun submit(runnable: Runnable?) {
        executor.execute(runnable)
    }

    fun postDelayed(runnable: Runnable?, delay: Long): ScheduledFuture<*>? {
        return scheduledExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS)
    }

    fun scheduleAtFixedRate(
        runnable: Runnable?,
        initialDelay: Long,
        delay: Long
    ): ScheduledFuture<*>? {
        return scheduledExecutorService.scheduleAtFixedRate(
            runnable,
            initialDelay,
            delay,
            TimeUnit.MILLISECONDS
        )
    }
}