/*
 * Copyright 2017 Andres.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eljavatar.swingutils.core.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Objects;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.text.View;

/**
 *
 * @author Andres Mauricio (http://www.eljavatar.com)
 */
public class LinkViewRadioButtonUI extends BasicRadioButtonUI {
//     private static final LinkViewRadioButtonUI radioButtonUI = new LinkViewRadioButtonUI();
//     private boolean defaults_initialized = false;
    private static final Dimension SIZE = new Dimension();
    private static final Rectangle VIEW_RECT = new Rectangle();
    private static final Rectangle ICON_RECT = new Rectangle();
    private static final Rectangle TEXT_RECT = new Rectangle();

//     public static ComponentUI createUI(JComponent b) {
//         return radioButtonUI;
//     }
//     @Override protected void installDefaults(AbstractButton b) {
//         super.installDefaults(b);
//         if (!defaults_initialized) {
//             icon = null; //UIManager.getIcon(getPropertyPrefix() + "icon");
//             defaults_initialized = true;
//         }
//     }
//     @Override protected void uninstallDefaults(AbstractButton b) {
//         super.uninstallDefaults(b);
//         defaults_initialized = false;
//     }
    @Override
    public Icon getDefaultIcon() {
        return null;
    }
    // [UnsynchronizedOverridesSynchronized] Unsynchronized method paint overrides synchronized method in BasicRadioButtonUI
    @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
    @Override
    public synchronized void paint(Graphics g, JComponent c) {
        //AbstractButton b = (AbstractButton) c;
        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = c.getFontMetrics(f);

        Insets i = c.getInsets();
        c.getSize(SIZE);
        VIEW_RECT.x = i.left;
        VIEW_RECT.y = i.top;
        VIEW_RECT.width = SIZE.width - i.right - VIEW_RECT.x;
        VIEW_RECT.height = SIZE.height - i.bottom - VIEW_RECT.y;
        ICON_RECT.setBounds(0, 0, 0, 0); //.x = iconRect.y = iconRect.width = iconRect.height = 0;
        TEXT_RECT.setBounds(0, 0, 0, 0); //.x = textRect.y = textRect.width = textRect.height = 0;

        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, SIZE.width, SIZE.height);
        }

        String text;
        AbstractButton b;
        if (c instanceof AbstractButton) {
            b = (AbstractButton) c;
            text = SwingUtilities.layoutCompoundLabel(
                b, fm, b.getText(), null, //altIcon != null ? altIcon : getDefaultIcon(),
                b.getVerticalAlignment(),    b.getHorizontalAlignment(),
                b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
                VIEW_RECT, ICON_RECT, TEXT_RECT,
                0); //b.getText() == null ? 0 : b.getIconTextGap());
        } else {
            return;
        }

//         // Changing Component State During Painting (an infinite repaint loop)
//         // pointed out by Peter
//         // -note: http://today.java.net/pub/a/today/2007/08/30/debugging-swing.html#changing-component-state-during-the-painting
//         //b.setForeground(Color.BLUE);
//         if (!model.isEnabled()) {
//             //b.setForeground(Color.GRAY);
//         } else if (model.isPressed() && model.isArmed() || model.isSelected()) {
//             //b.setForeground(Color.BLACK);
//         } else if (b.isRolloverEnabled() && model.isRollover()) {

        ButtonModel model = b.getModel();
        g.setColor(c.getForeground());
        if (!model.isSelected() && !model.isPressed() && !model.isArmed() && b.isRolloverEnabled() && model.isRollover()) {
            g.drawLine(VIEW_RECT.x,                VIEW_RECT.y + VIEW_RECT.height,
                       VIEW_RECT.x + VIEW_RECT.width, VIEW_RECT.y + VIEW_RECT.height);
        }
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (Objects.nonNull(v)) {
            v.paint(g, TEXT_RECT);
        } else {
            paintText(g, c, TEXT_RECT, text);
        }
    }
}
