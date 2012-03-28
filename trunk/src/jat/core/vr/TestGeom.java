package jat.core.vr;

import javax.media.j3d.*;

/**
 * Simple color-per-vertex cube with a different color for each face
 */
public class TestGeom extends  Body3D
{
	double scale;
	Shape3D s;

	private static final float[] verts = 
	{
		// front face
		1.0f, -1.0f, 1.0f, // 
		1.0f, 1.0f, 1.0f, //
		-1.0f, 1.0f, 1.0f, //
		-1.0f, -1.0f, 1.0f, //
		// back face
		-1.0f, -1.0f, -1.0f,//
		 -1.0f, 1.0f, -1.0f,//
		  1.0f, 1.0f, -1.0f, //
		  1.0f, -1.0f, -1.0f,//
		//		// right face
				1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f,
		//		// left face
		//		-1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f,
		//		// top face
		//		1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f,
		//		// bottom face
		//		-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 
	};

	private static final float[] colors = 
	{
		// front face (red)
		1.0f, 0.0f, 0.0f, // one 
		0.0f, 1.0f, 0.0f, // 
		1.0f, 0.0f, 0.0f, // 
		1.0f, 0.0f, 0.0f,
		// back face (green)
		0.0f, 1.0f, 0.0f,//
		 0.0f, 1.0f, 0.0f, //
		 0.0f, 1.0f, 0.0f,//
		  0.0f, 1.0f, 0.0f,//
		//		// right face (blue)
				0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
		//		// left face (yellow)
		//		1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
		//		// top face (magenta)
		//		1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f,
		//		// bottom face (cyan)
		//		0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 
	};

	public TestGeom()
	{
		scale = 1.0;
		s=new Shape3D();
		QuadArray cube = new QuadArray(24, QuadArray.COORDINATES | QuadArray.COLOR_3);

		cube.setCoordinates(0, verts);
		cube.setColors(0, colors);

		s.setGeometry(cube);
		addChild(s);

	}

	public TestGeom(double scale)
	{
		this.scale = scale;
		s=new Shape3D();

		QuadArray cube = new QuadArray(24, QuadArray.COORDINATES | QuadArray.COLOR_3);

		float scaledVerts[] = new float[verts.length];
		for (int i = 0; i < verts.length; i++)
			scaledVerts[i] = verts[i] * (float)scale;

		cube.setCoordinates(0, scaledVerts);
		cube.setColors(0, colors);

		s.setGeometry(cube);
		addChild(s);
	}

	/**
	 * @deprecated ColorCube now extends shape so it is no longer necessary
	 * to call this method.
	 */
//	public Shape3D getShape()
//	{
//		return this;
//	}
//
//	/**
//	 * Returns the scale of the Cube
//	 *
//	 * @since Java 3D 1.2.1
//	 */
//	public double getScale()
//	{
//		return scale;
//	}
}
