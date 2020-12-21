package com.panghu.uikit.executors

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class BgThreadPoolExecutor private constructor() : ThreadPoolExecutor(
    CORE_POOL_SIZE,
    MAXIMUM_POOL_SIZE,
    KEEP_ALIVE_SECONDS,
    TimeUnit.SECONDS,
    POOL_WORK_QUEUE
) {
    companion object {
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
        private val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))
        private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
        private const val KEEP_ALIVE_SECONDS = 10L
        private val POOL_WORK_QUEUE = LinkedBlockingQueue<Runnable>(128)

        @JvmStatic
        val instance: BgThreadPoolExecutor by lazy {
            BgThreadPoolExecutor()
        }
    }
}