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

                // common_google_play_services_enable_button
                if (text.equals("Enable")
                        /* cs */ || text.equals("Aktivovat") || text.equals("Povolit")
                        /* da */ || text.equals("Aktivér")
                        /* de */ || text.equals("Aktivieren")
                        /* es */ || text.equals("Habilitar")
                        /* es-us */ || text.equals("Activar")
                        /* fi */ || text.equals("Salli") || text.equals("Ota käyttöön")
                        /* fr */ || text.equals("Activer")
                        /* hi */ || text.equals("सक्षम करें") || text.equals("चालू कर")
                        /* in */ || text.equals("Aktifkan")
                        /* it */ || text.equals("Attiva")
                        /* ja */ || text.equals("有効にする") || text.equals("有効化")
                        /* ko */ || text.equals("사용") || text.equals("사용 설정")
                        /* nb */ || text.equals("Slå på")
                        /* nl */ || text.equals("Inschakelen")
                        /* pl */ || text.equals("Włącz")
                        /* pt */ || text.equals("Ativar")
                        /* ru */ || text.equals("Включить")
                        /* sv */ || text.equals("Aktivera")
                        /* th */ || text.equals("เปิดใช้งาน") || text.equals("เปิดใช้")
                        /* tr */ || text.equals("Etkinleştir")
                        /* vi */ || text.equals("Bật")
                        /* zh-cn */ || text.equals("启用")
                        /* zh-hk, zh-tw */ || text.equals("啟用"))
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
