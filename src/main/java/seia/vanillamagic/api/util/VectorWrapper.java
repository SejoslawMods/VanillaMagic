package seia.vanillamagic.api.util;

import net.minecraft.util.math.Vec3d;

/**
 * Class which is used to wrap Minecraft native Vector utils. Minecraft version
 * independent Vector wrapper. It will be independent if updated correctly.
 */
public class VectorWrapper {
	private VectorWrapper() {
	}

	/**
	 * Used to wrap single MC vector.
	 * 
	 * @return Returns wrapped vector.
	 */
	public static Vector3D wrap(double x, double y, double z) {
		return wrap(new Vec3d(x, y, z));
	}

	/**
	 * Used to wrap single MC vector.
	 * 
	 * @return Returns wrapped vector.
	 */
	public static Vector3D wrap(Vec3d minecraftVector) {
		return new Vector3D(minecraftVector);
	}

	/**
	 * Class which wraps native Minecraft vector.
	 */
	public static class Vector3D {
		private Vec3d vector;

		public Vector3D(Vec3d mcVector) {
			this.vector = mcVector;
		}

		/**
		 * @return Returns wrapped X coordinate.
		 */
		public double getX() {
			return this.vector.x;
		}

		/**
		 * @return Returns wrapped Y coordinate.
		 */
		public double getY() {
			return this.vector.y;
		}

		/**
		 * @return Returns wrapped Z coordinate.
		 */
		public double getZ() {
			return this.vector.z;
		}

		/**
		 * @return Returns wrapped Vec3d subtract method.
		 */
		public Vector3D subtract(Vector3D vec) {
			return VectorWrapper.wrap(this.vector.subtract(vec.vector));
		}

		/**
		 * @return Returns wrapped Vec3d normalize method.
		 */
		public Vector3D normalize() {
			return VectorWrapper.wrap(this.vector.normalize());
		}

		public double mag() {
			return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ());
		}
	}
}