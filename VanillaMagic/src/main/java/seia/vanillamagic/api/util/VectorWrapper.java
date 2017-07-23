package seia.vanillamagic.api.util;

import net.minecraft.util.math.Vec3d;

/**
 * Class which is used to wrap Minecraft native Vector utils.
 */
public class VectorWrapper 
{
	private VectorWrapper()
	{
	}
	
	/**
	 * Used to wrap single MC vector.
	 * 
	 * @return Returns wrapped vector.
	 */
	public static Vector3D wrap(double x, double y, double z)
	{
		return wrap(new Vec3d(x, y, z));
	}
	
	/**
	 * Used to wrap single MC vector.
	 * 
	 * @return Returns wrapped vector.
	 */
	public static Vector3D wrap(Vec3d minecraftVector)
	{
		return new Vector3D(minecraftVector);
	}
	
	/**
	 * Class which wraps native Minecraft vector.
	 */
	public static class Vector3D
	{
		private Vec3d _vector;
		
		public Vector3D(Vec3d mcVector)
		{
			this._vector = mcVector;
		}
		
		/**
		 * @return Returns wrapped X coordinate.
		 */
		public double getX()
		{
			return this._vector.x;
		}
		
		/**
		 * @return Returns wrapped Y coordinate.
		 */
		public double getY()
		{
			return this._vector.y;
		}
		
		/**
		 * @return Returns wrapped Z coordinate.
		 */
		public double getZ()
		{
			return this._vector.z;
		}
		
		/**
		 * @return Returns wrapped Vec3d subtract method.
		 */
		public Vector3D subtract(Vector3D vec)
		{
			return VectorWrapper.wrap(this._vector.subtract(vec._vector));
		}
		
		/**
		 * @return Returns wrapped Vec3d normalize method.
		 */
		public Vector3D normalize()
		{
			return VectorWrapper.wrap(this._vector.normalize());
		}
	}
}