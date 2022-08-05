package pixelj.models;

public record KerningPairRecord(int id, int left, int right, int value) {

    /**
     * @param pair Source
     * @return A record copying the source's data.
     */
    public static KerningPairRecord from(final KerningPair pair) {
        return new KerningPairRecord(
            pair.hashCode(),
            pair.getLeft().getCodePoint(),
            pair.getRight().getCodePoint(),
            pair.getKerningValue()
        );
    }
}
