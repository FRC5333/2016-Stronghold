package frc.team5333.core.control

import java.util.*

class ControlLease<T>(var obj: T) {

    object Priority {
        var LOWEST  = 20
        var LOWER   = 30
        var LOW     = 40
        var NORMAL  = 50
        var HIGH    = 60
        var HIGHER  = 70
        var HIGHEST = 80
    }

    class Lease<T>(var priority: Int, var __leaser: ControlLease<T>) {
        fun use(consumer: (T) -> Unit) = __leaser.use(this, consumer)
        fun release() = __leaser.release(this)
        fun isActive(): Boolean = __leaser.isActive(this)
        fun get(): Optional<T> = __leaser.get(this)
    }

    private var active_priority = -1
    private var last_update = 0L

    fun acquire(priority: Int): Lease<T> = Lease(priority, this)

    fun release(lease: Lease<T>) {
        if (active_priority == lease.priority) active_priority = -1
    }

    fun use(lease: Lease<T>, consumer: (T) -> Unit) {
        var now = System.currentTimeMillis()
        if (lease.priority >= active_priority || now - last_update > 100) {
            last_update = now
            active_priority = lease.priority
            consumer.invoke(obj)
        }
    }

    fun isActive(lease: Lease<T>): Boolean = lease.priority >= active_priority

    fun get(lease: Lease<T>): Optional<T> {
        var now = System.currentTimeMillis()
        if (lease.priority >= active_priority || now - last_update > 100) {
            last_update = now
            active_priority = lease.priority
            return Optional.of(obj)
        }
        return Optional.empty()
    }
}