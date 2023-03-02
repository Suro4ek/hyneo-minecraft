package eu.suro.utils.math

class Vector3D constructor(var x: Double,var y: Double,var z: Double): Cloneable {

    companion object{
        val ZERO = Vector3D(0.0,0.0,0.0)
    }

    fun add(x: Double, y: Double, z: Double): Vector3D {
        this.x += x
        this.y += y
        this.z += z
        
        return this
    }

    fun add(vec: Vector3D): Vector3D {
        this.x += vec.x
        this.y += vec.y
        this.z += vec.z

        return this
    }

    fun subtract(x: Double, y: Double, z: Double): Vector3D {
        this.x -= x
        this.y -= y
        this.z -= z

        return this
    }

    fun subtract(vec: Vector3D): Vector3D {
        this.x -= vec.x
        this.y -= vec.y
        this.z -= vec.z

        return this
    }

    fun dot(vector: Vector3D): Double {
        return x * vector.x + y * vector.y + z * vector.z
    }

    fun length(): Double {
        return Math.sqrt(lengthSquared())
    }

    fun lengthSquared(): Double {
        return x * x + y * y + z * z
    }

    fun distance(o: Vector3D): Double {
        return Math.sqrt(distanceSquared(o))
    }

    fun distanceSquared(o: Vector3D): Double {
        val deltaX = x - o.x
        val deltaY = y - o.y
        val deltaZ = z - o.z

        return deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ
    }

    fun multiply(multiply: Double): Vector3D {
        this.x *= multiply
        this.y *= multiply
        this.z *= multiply

        return this
    }

    fun normalize(): Vector3D {
        val length = length()

        x /= length
        y /= length
        z /= length

        return this
    }

    override fun clone(): Vector3D {
        return Vector3D(x, y, z)
    }

    fun invert(): Vector3D {
        this.x = -x
        this.y = -y
        this.z = -z

        return this
    }
}