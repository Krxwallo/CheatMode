package com.justAm0dd3r.cheatmode.gui.button;

import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.CheckReturnValue;

public class ToggleButton extends Button {
    private boolean state;
    private final String mainTitle;
    public ToggleButton(int x, int y, int width, int height, String mainTitle, boolean startState, IPressable pressedAction) {
        super(x, y, width, height, new StringTextComponent(getText(mainTitle, startState)), pressedAction);
        this.mainTitle = mainTitle;
        this.state = startState;
    }

    private static String getText(String mainTitle, boolean state) {
        return mainTitle + (state ? ": ON" : ": OFF");
    }

    @CheckReturnValue
    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public void onPress() {
        state = !state;
        this.setMessage(new StringTextComponent(getText(mainTitle, state)));
        super.onPress();
    }
}
