package us.xingrz.gzmtr.data;

import android.graphics.Color;

import java.util.LinkedList;
import java.util.List;

public class Line {
    public Symbols symbols;
    public Status status;
    public String url;
    public List<String> interchanges;
    public Fleet fleet;
    public Speed speed;
    public Block block;
    public Train train;
    public Electrify electrify;
    public List<Form> forms;
    public List<List<String>> routes;
    public List<String> depots;

    public static class Symbols {
        public String name;
        public String full;
        public String alias;
        public LinkedList<Object> color;

        public int getColor() {
            if (color.size() == 4 && "RGB".equals(color.get(0))) {
                return Color.rgb(
                        ((Double) color.get(1)).intValue(),
                        ((Double) color.get(2)).intValue(),
                        ((Double) color.get(3)).intValue()
                );
            } else {
                return 0;
            }
        }
    }

    public static enum Status {
        PLANNING,
        CONSTRUCTION,
        OPERATION
    }

    public static class Fleet {
        public int size;
        public Type type;

        public static enum Type {
            A, B, L
        }
    }

    public static class Speed {
        public int max;
    }

    public static class Block {
        public String vendor;
        public Type type;

        public static enum Type {
            FTGS, CBTC
        }
    }

    public static class Train {
        public List<String> types;
    }

    public static class Electrify {
        public List<Form> forms;
        public Type type;
        public int voltage;

        public static enum Form {
            OVERHEAD_CABLE,
            OVERHEAD_TRACK,
            TRACK_SINGLE,
            TRACK_DOUBLE
        }

        public static enum Type {
            DC, AC
        }
    }

    public static enum Form {
        TUNNEL, GROUND, VIADUCT
    }

}
