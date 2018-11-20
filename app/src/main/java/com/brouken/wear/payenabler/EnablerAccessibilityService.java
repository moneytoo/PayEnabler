package com.brouken.wear.payenabler;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class EnablerAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        final AccessibilityNodeInfo topParent = getTopmostParent(accessibilityEvent.getSource());

        if (isPayInHierarchy(topParent))
            goThroughHierarchy(topParent);
    }

    private AccessibilityNodeInfo getTopmostParent(final AccessibilityNodeInfo node) {
        final AccessibilityNodeInfo parent = node.getParent();

        if (parent == null)
            return node;
        else
            return getTopmostParent(parent);
    }

    private void goThroughHierarchy(final AccessibilityNodeInfo nodeInfo) {
        final int count = nodeInfo.getChildCount();

        for (int i = 0; i < count; i++) {
            final AccessibilityNodeInfo child = nodeInfo.getChild(i);

            if (child == null)
                continue;

            goThroughHierarchy(child);

            final CharSequence sequence = child.getText();
            if (sequence != null) {
                final String text = sequence.toString();

                if (text.equals("Enable"))
                    clickClickableParent(child);
            }
        }
    }

    private void clickClickableParent(final AccessibilityNodeInfo nodeInfo) {
        final AccessibilityNodeInfo parent = nodeInfo.getParent();

        if (parent == null)
            return;

        if (parent.isClickable())
            parent.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId());
        else
            clickClickableParent(parent);
    }

    private boolean isPayInHierarchy(final AccessibilityNodeInfo nodeInfo) {
        if (isPayInNode(nodeInfo))
            return true;

        final int count = nodeInfo.getChildCount();

        for (int i = 0; i < count; i++) {
            final AccessibilityNodeInfo child = nodeInfo.getChild(i);

            if (child == null)
                continue;

            if (isPayInHierarchy(child))
                return true;
            else {
                if (isPayInNode(child))
                    return true;
            }
        }

        return false;
    }

    private boolean isPayInNode(final AccessibilityNodeInfo nodeInfo) {
        final CharSequence sequence = nodeInfo.getText();
        if (sequence != null && sequence.toString().equals("Google Pay")) {
        //if (sequence != null && sequence.toString().startsWith("Runtastic Running App")) {
            return true;
        }
        return false;
    }

    @Override
    public void onInterrupt() {

    }
}
