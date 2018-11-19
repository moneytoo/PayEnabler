package com.brouken.wear.payenabler;

import android.accessibilityservice.AccessibilityService;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class EnablerAccessibilityService extends AccessibilityService {

    private Handler mHandler = new Handler();
    private boolean callbackWaiting = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        // FIXME: Work around issues after (some) Play Store fresh starts
        if (!callbackWaiting) {
            callbackWaiting = true;
            mHandler.postDelayed(scan, 2000);
        }
    }

    private void goThroughHierarchy(AccessibilityNodeInfo nodeInfo) {
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

    private Runnable scan = new Runnable() {
        @Override
        public void run() {
            final AccessibilityNodeInfo root = getRootInActiveWindow();

            if (isPayInHierarchy(root))
                goThroughHierarchy(root);

            callbackWaiting = false;
        }
    };

    private void clickClickableParent(AccessibilityNodeInfo nodeInfo) {
        final AccessibilityNodeInfo parent = nodeInfo.getParent();

        if (parent == null)
            return;

        if (parent.isClickable())
            parent.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK.getId());
        else
            clickClickableParent(parent);
    }

    private boolean isPayInHierarchy(AccessibilityNodeInfo nodeInfo) {
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

    private boolean isPayInNode(AccessibilityNodeInfo nodeInfo) {
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
