package io.github.mimoguz.pixelj.controls;

import java.awt.Color;

import org.eclipse.jdt.annotation.NonNull;

public record Line(@NonNull Orientation orientation, int point, @NonNull Color color) {
}
