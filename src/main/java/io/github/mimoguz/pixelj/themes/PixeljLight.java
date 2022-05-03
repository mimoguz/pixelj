package io.github.mimoguz.pixelj.themes;

import com.formdev.flatlaf.FlatLightLaf;

public class PixeljLight
	extends FlatLightLaf
{
	public static final String NAME = "PixeljLight";

	public static boolean setup() {
		return setup( new PixeljLight() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, PixeljLight.class );
	}

	@Override
	public String getName() {
		return NAME;
	}
}
