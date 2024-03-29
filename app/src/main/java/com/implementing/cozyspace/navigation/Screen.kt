package com.implementing.cozyspace.navigation

import com.implementing.cozyspace.util.Constants


sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Splash : Screen("splash_screen")
    object SpacesScreen : Screen("spaces_screen")
    object DashboardScreen : Screen("dashboard_screen")
    object SettingsScreen : Screen("settings_screen")

    /*
       Bookmarks Section
   */

    object BookmarksScreen : Screen("bookmarks_screen")

    object BookmarkDetailScreen : Screen("bookmark_detail_screen/{${Constants.BOOKMARK_ID_ARG}}")

    object BookmarkSearchScreen : Screen("bookmark_search_screen")

    /*
       Diary Section
   */
    object DiaryScreen : Screen("diary_screen")

    object DiaryDetailScreen : Screen("diary_detail_screen/{${Constants.DIARY_ID_ARG}}")

    object DiarySearchScreen : Screen("diary_search_screen")

    object DiaryChartScreen : Screen("diary_chart_screen")


    /*
       Notes Section
   */
    object NotesScreen : Screen("notes_screen")

    object NoteDetailsScreen : Screen("note_detail_screen/{${Constants.NOTE_ID_ARG}}?${Constants.FOLDER_ID}={${Constants.FOLDER_ID}}")

    object NoteSearchScreen : Screen("note_search_screen")

    object NoteFolderDetailsScreen : Screen("note_folder_details_screen/{${Constants.FOLDER_ID}}")


    /*
       Tasks Section
   */
   object TasksScreen : Screen("tasks_screen?${Constants.ADD_TASK_ARG}={${Constants.ADD_TASK_ARG}}")
   object TaskDetailScreen : Screen("task_detail_screen/{${Constants.TASK_ID_ARG}}")
   object TaskSearchScreen : Screen("task_search_screen")




    /*
        Calendar
    */

    object CalendarScreen : Screen("calendar_screen")
    object CalendarEventDetailsScreen : Screen("calendar_event_details_screen/{${Constants.CALENDAR_EVENT_ARG}}")

    object DoodleScreen : Screen("doodle_screen")
    object SketchGuess : Screen("sketchguess_screen")

    object ImportExportScreen : Screen("import_export_screen")

}