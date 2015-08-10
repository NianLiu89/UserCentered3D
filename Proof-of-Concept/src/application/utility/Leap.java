package application.utility;

import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;

public class Leap {

    public static Hand getRightHand(Frame frame) {
        return frame.hands().get(0).isRight() ? frame.hands().get(0) : frame
                .hands().get(1);
    }
}
