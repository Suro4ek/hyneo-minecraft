package eu.suro.utils.math

class Vector3Df constructor(var x: Float, var y: Float, var z: Float) : Cloneable {

    public override fun clone(): Vector3Df {
        return Vector3Df(x, y, z)
    }

    fun invert(): Vector3Df {
        x = -x
        y = -y
        z = -z
        return this
    }

    fun add(x: Float, y: Float, z: Float): Vector3Df {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(vec: Vector3Df): Vector3Df {
        x += vec.x
        y += vec.y
        z += vec.z
        return this
    }

    fun subtract(x: Float, y: Float, z: Float): Vector3Df {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun subtract(vec: Vector3Df): Vector3Df {
        x -= vec.x
        y -= vec.y
        z -= vec.z
        return this
    }

    fun multiply(multiply: Float): Vector3Df {
        x *= multiply
        y *= multiply
        z *= multiply
        return this
    }

    fun length(): Float {
        return Math.sqrt(lengthSquared().toDouble()).toFloat()
    }

    fun lengthSquared(): Float {
        return x * x + y * y + z * z
    }

    fun distance(o: Vector3Df): Float {
        return Math.sqrt(distanceSquared(o).toDouble()).toFloat()
    }

    fun distanceSquared(o: Vector3Df): Float {
        val deltaX = x - o.x
        val deltaY = y - o.y
        val deltaZ = z - o.z
        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ
    }

    fun normalize(): Vector3Df {
        val length = length()
        x /= length
        y /= length
        z /= length
        return this
    }

    fun toVector3D(): Vector3D {
        return Vector3D(x.toDouble(), y.toDouble(), z.toDouble())
    }
}