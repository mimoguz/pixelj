package pixelj.models;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import pixelj.graphics.BinaryImage;

public record CompressedGlyph(int codePoint, int width, byte[] imageBytes) {

    /**
     * @param glyph Source
     * @return Compressed glyph using the source's data
     */
    public static CompressedGlyph from(final Glyph glyph) {
        final var deflater = new Deflater();
        final var image = glyph.getImage();
        final var inBytes = new byte[image.getImageWidth() * image.getImageHeight()];
        image.getDataElements(0, 0, image.getImageWidth(), image.getImageHeight(), inBytes);
        final var outBuffer = new byte[inBytes.length];
        deflater.setInput(inBytes);
        deflater.finish();
        final var outLength = deflater.deflate(outBuffer);
        final var outBytes = Arrays.copyOfRange(outBuffer, 0, outLength);
        return new CompressedGlyph(glyph.getCodePoint(), glyph.getWidth(), outBytes);
    }

    /**
     * @param imageWidth  Target image width
     * @param imageHeight Target image height
     * @return Decompressed glyph
     * @throws MisshapenDataException If the data size doesn't match with the target image size or
     *                                the data is corrupted.
     */
    public Glyph decompress(final int imageWidth, final int imageHeight) throws MisshapenDataException {
        final var inflater = new Inflater();
        final var outBytes = new byte[imageWidth * imageHeight];
        inflater.setInput(imageBytes);
        try {
            final var outLength = inflater.inflate(outBytes);
            if (outLength != outBytes.length) {
                throw new MisshapenDataException("Inflated data size doesn't match with expected image size");
            }
            final var image = BinaryImage.of(imageWidth, imageHeight, false);
            image.setDataElements(0, 0, imageWidth, imageHeight, outBytes);
            return new Glyph(codePoint, width, image);
        } catch (DataFormatException exception) {
            throw new MisshapenDataException("Can't inflate input:\n" + exception.getMessage());
        }
    }

    public static class MisshapenDataException extends Exception {
        public MisshapenDataException(final String message) {
            super(message);
        }
    }
}
