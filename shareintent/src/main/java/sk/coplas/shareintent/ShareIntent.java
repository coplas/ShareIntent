package sk.coplas.shareintent;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author CoPLaS
 */
public class ShareIntent {

    private Collection intents;

    public Collection getIntents() {
        return intents;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Intents: ");
        if (intents != null) {
            sb.append(intents.size());
        } else {
            sb.append(0);
        }
        return sb.toString();
    }

    private ShareIntent() {
    }

    /**
     *
     * The builder for Share Intent class.
     *
     */
    public static class ShareIntentBuilder {

        final String[] twitterApps = { "com.twitter.android", "com.handmark.tweetcaster", "com.seesmic", "com.thedeck.android", "com.levelup.touiteur", "com.thedeck.android.app" };
        final String[] facebookApps = { "com.facebook.katana" };

        private SocialNetwork socialNetwork;
        private Context context;
        private String type;

        public static final String TYPE_TEXT_PLAIN = "text/plain";


        public ShareIntentBuilder(Context context, SocialNetwork socialNetwork) {
            if (context == null ) {
                throw new NullPointerException(
                        "context can not be null");
            }
            if (socialNetwork == null ) {
                throw new NullPointerException(
                        "SocialNetwork can not be null");
            }
            this.socialNetwork = socialNetwork;
            this.context = context;
        }

        public ShareIntentBuilder type(String type) {
            this.type = type;
            return this;
        }

        public ShareIntentBuilder typeText() {
            this.type = TYPE_TEXT_PLAIN;
            return this;
        }

        public Collection<Intent> findClient(final String[] packageNames) {
            Collection<Intent> res = new ArrayList<Intent>();
            Intent intent = new Intent(Intent.ACTION_SEND);
            final PackageManager packageManager = context.getPackageManager();
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

            for (int i = 0; i < packageNames.length; i++) {
                for (ResolveInfo resolveInfo : list) {
                    String p = resolveInfo.activityInfo.packageName;
                    if (p != null && p.startsWith(packageNames[i])) {
                        intent.setPackage(p);
                        if (type != null) {
                            intent.setType(type);
                        }
                        res.add(intent);
                    }
                }
            }
            return res;
        }

        public ShareIntent build(){
            ShareIntent shareIntent = new ShareIntent();
            if (SocialNetwork.TWITTER.equals(socialNetwork)) {
                shareIntent.intents = findClient(twitterApps);
            } else if (SocialNetwork.FACEBOOK.equals(socialNetwork)) {
                shareIntent.intents = findClient(facebookApps);
            }

            return shareIntent;
        }
    }

}
