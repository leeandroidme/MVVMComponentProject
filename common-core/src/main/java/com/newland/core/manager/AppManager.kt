package com.newland.core.manager;

import android.app.Activity

object AppManager {
  private val activities = mutableListOf<Activity>()

  fun addActivity(activity: Activity) {
    activities.remove(activity)
    activities.add(activity)
  }
  fun removeActivity(activity: Activity) {
    if (activities.contains(activity)) {
      activities.remove(activity)
    }
  }
  fun finishActivity(activity: Activity) {
    if (activities.contains(activity)) {
      activities.remove(activity)
      activity.finish()
    }
  }

  fun currentActivity(): Activity? =
    if (activities.isEmpty()) null else activities[activities.size - 1]

  fun finishAll() =
    activities.filterNot { it.isFinishing }.forEach { it.finish() }

  fun getActivty(clazz:Class<Activity> ):Activity? {
    for (activity:Activity in  activities) {
      if (activity.javaClass.equals(clazz)) {
        return activity;
      }
    }
    return null;
  }
}
