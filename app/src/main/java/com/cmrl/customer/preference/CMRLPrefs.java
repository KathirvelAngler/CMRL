package com.cmrl.customer.preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mathan on 08-07-2019.
 */

public class CMRLPrefs {
	private static final String DEFAULT_SUFFIX = "_aqprefs";
	private static final String LENGTH = "#LENGTH";
	private static SharedPreferences mPrefs;
	private static Gson GSON = new Gson();

	/**
	 * Initialize the Prefs helper class to keep a reference to the
	 * SharedPreference for this application the SharedPreference will use the
	 * package name of the application as the Key.
	 * 
	 * This method
	 * 
	 * @param context
	 *            the Application context.
	 */
	@Deprecated
	public static void initPrefs(Context context) {
		new Builder().setContext(context).build();
	}

	/**
	 * @hide
	 */
	private static void initPrefs(Context context, String prefsName, int mode) {
		mPrefs = context.getSharedPreferences(prefsName, mode);
	}

	/**
	 * Returns an instance of the shared preference for this app.
	 * 
	 * @return an Instance of the SharedPreference
	 * 
	 * @throws RuntimeException
	 *             if sharedpreference instance has not been instatiated yet.
	 */
	public static SharedPreferences getPreferences() {
		if (mPrefs != null) {
			return mPrefs;
		}
		throw new RuntimeException(
				"Prefs class not correctly instantiated please call Builder.setContext().build(); in the Application class onCreate.");
	}

	/**
	 * @return Returns a map containing a list of pairs key/value representing
	 *         the preferences.
	 * @see SharedPreferences#getAll()
	 */
	public static Map<String, ?> getAll() {
		return getPreferences().getAll();
	}

	/**
	 * @param key
	 *            The name of the preference to retrieve.
	 * @param defValue
	 *            Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not an int.
	 * @see SharedPreferences#getInt(String, int)
	 */
	public static int getInt(final String key, final int defValue) {
		return getPreferences().getInt(key, defValue);
	}

	/**
	 * @param key
	 *            The name of the preference to retrieve.
	 * @param defValue
	 *            Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean.
	 * @see SharedPreferences#getBoolean(String, boolean)
	 */
	public static boolean getBoolean(final String key, final boolean defValue) {
		return getPreferences().getBoolean(key, defValue);
	}

	/**
	 * @param key
	 *            The name of the preference to retrieve.
	 * @param defValue
	 *            Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a long.
	 * @see SharedPreferences#getLong(String, long)
	 */
	public static long getLong(final String key, final long defValue) {
		return getPreferences().getLong(key, defValue);
	}

	/**
	 * Returns the double that has been saved as a long raw bits value in the
	 * long preferences.
	 * 
	 * @param key
	 *            The name of the preference to retrieve.
	 * @param defValue
	 *            the double Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a long.
	 * @see SharedPreferences#getLong(String, long)
	 */
	public static double getDouble(final String key, final double defValue) {
		return Double.longBitsToDouble(getPreferences().getLong(key,
				Double.doubleToLongBits(defValue)));
	}

	/**
	 * @param key
	 *            The name of the preference to retrieve.
	 * @param defValue
	 *            Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a float.
	 * @see SharedPreferences#getFloat(String, float)
	 */
	public static float getFloat(final String key, final float defValue) {
		return getPreferences().getFloat(key, defValue);
	}

	/**
	 * @param key
	 *            The name of the preference to retrieve.
	 * @param defValue
	 *            Value to return if this preference does not exist.
	 * @return Returns the preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a String.
	 * @see SharedPreferences#getString(String, String)
	 */
	public static String getString(final String key, final String defValue) {
		return getPreferences().getString(key, defValue);
	}

	/**
	 * @param key
	 *            The name of the preference to retrieve.
	 * @param defValue
	 *            Value to return if this preference does not exist.
	 * @return Returns the preference values if they exist, or defValues. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a Set.
	 * @see SharedPreferences#getStringSet(String,
	 *      Set)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Set<String> getStringSet(final String key,
                                           final Set<String> defValue) {
		SharedPreferences prefs = getPreferences();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return prefs.getStringSet(key, defValue);
		} else {
			if (prefs.contains(key + LENGTH)) {
				HashSet<String> set = new HashSet<>();
				// Workaround for pre-HC's lack of StringSets
				int stringSetLength = prefs.getInt(key + LENGTH, -1);
				if (stringSetLength >= 0) {
					for (int i = 0; i < stringSetLength; i++) {
						prefs.getString(key + "[" + i + "]", null);
					}
				}
				return set;
			}
		}
		return defValue;
	}

	public static <T> T getObject(String key, Class<T> a) {
		SharedPreferences prefs = getPreferences();
		String gson = prefs.getString(key, null);
		if (gson == null) {
			return null;
		} else {
			try {
				return GSON.fromJson(gson, a);
			} catch (Exception e) {
				throw new IllegalArgumentException("Object storaged with key "
						+ key + " is instanceof other class");
			}
		}
	}

	/**
	 * @param key
	 *            The name of the preference to modify.
	 * @param value
	 *            The new value for the preference.
	 * @see Editor#putLong(String, long)
	 */
	public static void putLong(final String key, final long value) {
		final Editor editor = getPreferences().edit();
		editor.putLong(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key
	 *            The name of the preference to modify.
	 * @param value
	 *            The new value for the preference.
	 * @see Editor#putInt(String, int)
	 */
	public static void putInt(final String key, final int value) {
		final Editor editor = getPreferences().edit();
		editor.putInt(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * Saves the double as a long raw bits inside the preferences.
	 * 
	 * @param key
	 *            The name of the preference to modify.
	 * @param value
	 *            The double value to be save in the preferences.
	 * @see Editor#putLong(String, long)
	 */
	public static void putDouble(final String key, final double value) {
		final Editor editor = getPreferences().edit();
		editor.putLong(key, Double.doubleToRawLongBits(value));
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key
	 *            The name of the preference to modify.
	 * @param value
	 *            The new value for the preference.
	 * @see Editor#putFloat(String, float)
	 */
	public static void putFloat(final String key, final float value) {
		final Editor editor = getPreferences().edit();
		editor.putFloat(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key
	 *            The name of the preference to modify.
	 * @param value
	 *            The new value for the preference.
	 * @see Editor#putBoolean(String, boolean)
	 */
	public static void putBoolean(final String key, final boolean value) {
		final Editor editor = getPreferences().edit();
		editor.putBoolean(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key
	 *            The name of the preference to modify.
	 * @param value
	 *            The new value for the preference.
	 * @see Editor#putString(String, String)
	 */
	public static void putString(final String key, final String value) {
		final Editor editor = getPreferences().edit();
		editor.putString(key, value);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key
	 *            The name of the preference to modify.
	 * @param value
	 *            The new value for the preference.
	 * @see Editor#putStringSet(String,
	 *      Set)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void putStringSet(final String key, final Set<String> value) {
		final Editor editor = getPreferences().edit();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			editor.putStringSet(key, value);
		} else {
			// Workaround for pre-HC's lack of StringSets
			int stringSetLength = 0;
			if (mPrefs.contains(key + LENGTH)) {
				// First read what the value was
				stringSetLength = mPrefs.getInt(key + LENGTH, -1);
			}
			editor.putInt(key + LENGTH, value.size());
			int i = 0;
			for (String aValue : value) {
				editor.putString(key + "[" + i + "]", aValue);
				i++;
			}
			for (; i < stringSetLength; i++) {
				// Remove any remaining values
				editor.remove(key + "[" + i + "]");
			}
		}
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	public static void putObject(String key, Object object) {
		final Editor editor = getPreferences().edit();

		if (object == null) {
			throw new IllegalArgumentException("object is null");
		}

		if (key.equals("") || key == null) {
			throw new IllegalArgumentException("key is empty or null");
		}

		editor.putString(key, GSON.toJson(object));

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key
	 *            The name of the preference to remove.
	 * @see Editor#remove(String)
	 */
	public static void remove(final String key) {
		SharedPreferences prefs = getPreferences();
		final Editor editor = prefs.edit();
		if (prefs.contains(key + LENGTH)) {
			// Workaround for pre-HC's lack of StringSets
			int stringSetLength = prefs.getInt(key + LENGTH, -1);
			if (stringSetLength >= 0) {
				editor.remove(key + LENGTH);
				for (int i = 0; i < stringSetLength; i++) {
					editor.remove(key + "[" + i + "]");
				}
			}
		}
		editor.remove(key);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
			editor.commit();
		} else {
			editor.apply();
		}
	}

	/**
	 * @param key
	 *            The name of the preference to check.
	 * @see SharedPreferences#contains(String)
	 * @return true if preference contains this key value.
	 */
	public static boolean contains(final String key) {
		return getPreferences().contains(key);
	}

	/**
	 * @see Editor#clear()
	 * @return the {@link Editor} that has been cleared.
	 */
	public static Editor clear() {
		return getPreferences().edit().clear();
	}

	/**
	 * Builder class for the EasyPrefs instance. You only have to call this once
	 * in the Application onCreate. And in the rest of the code base you can
	 * call Prefs.method name.
	 */
	public final static class Builder {
		private String mKey;
		private Context mContext;
		private int mMode = -1;
		private boolean mUseDefault = false;

		/**
		 * Set the filename of the sharedprefence instance usually this is the
		 * applications packagename.xml but for migration purposes or
		 * customization.
		 * 
		 * @param prefsName
		 *            the filename used for the sharedpreference
		 * @return the {@linkcom.pixplicity.easyprefs.library.Prefs.Builder}
		 *         object.
		 */
		public Builder setPrefsName(final String prefsName) {
			mKey = prefsName;
			return this;
		}

		/**
		 * Set the context used to instantiate the sharedpreferences
		 * 
		 * @param context
		 *            the application context
		 * @return the {@linkcom.pixplicity.easyprefs.library.Prefs.Builder}
		 *         object.
		 */
		public Builder setContext(final Context context) {
			mContext = context;
			return this;
		}

		/**
		 * Set the mode of the sharedpreference instance.
		 * 
		 * @param mode
		 *            Operating mode. Use 0 or {@link Context#MODE_PRIVATE} for
		 *            the default operation, {@link Context#MODE_WORLD_READABLE}
		 * @return the {@linkcom.pixplicity.easyprefs.library.Prefs.Builder}
		 *         object.
		 * @see Context#getSharedPreferences
		 */
		public Builder setMode(final int mode) {
			if (mode == ContextWrapper.MODE_PRIVATE
					|| mode == ContextWrapper.MODE_MULTI_PROCESS) {
				mMode = mode;
			} else {
				throw new RuntimeException(
						"The mode in the sharedpreference can only be set too ContextWrapper.MODE_PRIVATE, ContextWrapper.MODE_WORLD_READABLE, ContextWrapper.MODE_WORLD_WRITEABLE or ContextWrapper.MODE_MULTI_PROCESS");
			}

			return this;
		}

		/**
		 * Set the default sharedpreference file name. Often the package name of
		 * the application is used, but if the
		 * {@link android.preference.PreferenceActivity} or
		 * {@link android.preference.PreferenceFragment} is used android append
		 * that with _preference.
		 * 
		 * @param defaultSharedPreference
		 *            true if default sharedpreference name should used.
		 * @return the {@linkcom.pixplicity.easyprefs.library.Prefs.Builder}
		 *         object.
		 */
		public Builder setUseDefaultSharedPreference(
				boolean defaultSharedPreference) {
			mUseDefault = defaultSharedPreference;
			return this;
		}

		/**
		 * Initialize the sharedpreference instance to used in the application.
		 * 
		 * @throws RuntimeException
		 *             if context has not been set.
		 */
		public void build() {
			if (mContext == null) {
				throw new RuntimeException(
						"Context not set, please set context before building the Prefs instance.");
			}

			if (TextUtils.isEmpty(mKey)) {
				mKey = mContext.getPackageName();
			}

			if (mUseDefault) {
				mKey += DEFAULT_SUFFIX;
			}

			if (mMode == -1) {
				mMode = ContextWrapper.MODE_PRIVATE;
			}

			CMRLPrefs.initPrefs(mContext, mKey, mMode);
		}
	}
}