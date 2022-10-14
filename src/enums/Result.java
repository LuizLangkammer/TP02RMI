package enums;

public enum Result {

        NOTYOURTURN((byte)9),
        ALREADYOPEN((byte)2),
        HIT((byte)5), NOTHIT((byte)6),
        WON((byte)7), LOST((byte)8);

        private final byte value;

        Result(byte value){
                this.value=value;
        }

        public byte getValue() {
                return value;
        }
}
