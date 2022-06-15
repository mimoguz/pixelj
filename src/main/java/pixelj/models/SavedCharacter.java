package pixelj.models;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import pixelj.graphics.BinaryImage;

public record SavedCharacter(int codePoint, int width, byte[] imageBytes) {

    public static SavedCharacter from(CharacterItem character) {
        final var deflater = new Deflater();
        final var image = character.getGlyph();
        final var inBytes = new byte[image.getWidth() * image.getHeight()];
        image.getDataElements(0, 0, image.getWidth(), image.getHeight(), inBytes);
        final var outBuffer = new byte[inBytes.length];
        deflater.setInput(inBytes);
        deflater.finish();
        final var outLength = deflater.deflate(outBuffer);
        final var outBytes = Arrays.copyOfRange(outBuffer, 0, outLength);
        return new SavedCharacter(character.getCodePoint(), character.getWidth(), outBytes);
    }

    public CharacterItem getCharacter(int imageWidth, int imageHeight) throws MisshapenDataException {
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
            return new CharacterItem(codePoint, width, image);
        } catch (DataFormatException exception) {
            throw new MisshapenDataException("Can't inflate input:\n" + exception.getMessage());
        }
    }

    public static class MisshapenDataException extends Exception {
        public MisshapenDataException(String message) {
            super(message);
        }
    }
}
