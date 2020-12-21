package com.panghu.uikit.base.analytics;

import android.content.Context;

import com.panghu.uikit.R;

/**
 * Created by dennis.jiang on 8/24/16.
 */
public class ScreenName {
    /*Authentication Screens*/
    public static final String WELCOME_SCREEN = "Welcome";
    public static final String SIGN_UP_WITH_EMAIL = "Email Address";
    public static final String ACTIVATE_YOUR_ACCOUNT = "Activate Account";
    public static final String GUIDE_SCREEN = "Glip_Mobile_permissionScreen_viewedScreen";

    public static final String FIRST_PRODUCT_TOUR = "First Product Tour";
    public static final String SECOND_PRODUCT_TOUR = "Second Product Tour";
    public static final String THIRD_PRODUCT_TOUR = "Third Product Tour";
    public static final String FOURTH_PRODUCT_TOUR = "Fourth Product Tour";

    public static final String EMERGENCY_SERVICES_DISCLAIMER = "Emergency Services Disclaimer";
    public static final String ACCOUNT_SETUP = "Account Setup";
    public static final String UNIFIED_LOGIN = "Unified Login";
    private static final String TERMS_OF_SERVICE_SCREEN = "Terms of Service";
    private static final String PRIVACY_NOTE_SCREEN = "Privacy Note";
    private static final String ACCEPTABLE_USE_POLICY_SCREEN = "Acceptable Use Policy";

    /*Messages Screens*/
    public static final String MESSAGES_TAB = "Messages Tab";
    public static final String ALL_MESSAGES_SCREEN = "All Messages";
    public static final String FAVORITE_MESSAGES_SCREEN = "Favorite Messages";
    public static final String PEOPLE_MESSAGES_SCREEN = "Direct Messages";
    public static final String TEAMS_MESSAGES_SCREEN = "Teams Messages";
    // Different with iOS version,only search home screen.
    public static final String NEW_MESSAGES_SCREEN = "New Messages";
    public static final String SINGLE_CONVERSATION_SCREEN = "Single Conversation";
    public static final String GROUP_CONVERSATION_SCREEN = "Group Conversation";
    public static final String TEAM_CONVERSATION_SCREEN = "Team Conversation";
    public static final String EVERYONE_CONVERSATION_SCREEN = "Everyone Conversation";
    public static final String ME_CONVERSATION_SCREEN = "Me Conversation Thread";
    public static final String SHELF_TASKS_SCREEN = "Shelf Tasks";
    public static final String SHELF_EVENTS_SCREEN = "Shelf Events";
    public static final String SHELF_NOTES_SCREEN = "Shelf Notes";
    public static final String SHELF_FILES_SCREEN = "Shelf Files";
    public static final String SHELF_LINKS_SCREEN = "Shelf Links";
    public static final String ADD_MEMBER_SCREEN = "Add Member";
    public static final String NOTE_DETAILS_SCREEN = "Note Details";
    public static final String ACTION_SEND_SCREEN = "Share to Glip";
    public static final String LIKES = "Likes";

    /*Contacts Screen*/
    public static final String CONTACTS_TAB = "Contacts Tab";
    public static final String INVITE_TO_GLIP = "Invite People";
    public static final String INVITE_FROM_CONTACT = "Invite Contacts";
    public static final String COMPANY_CONTACTS = "Glip_Mobile_contacts_companyContacts";
    public static final String GUEST_CONTACTS = "Glip_Mobile_contacts_guestsContacts";
    public static final String PERSONAL_CONTACTS = "Glip_Mobile_contacts_personalContacts";
    public static final String TEAMS_CONTACTS = "Glip_Mobile_contacts_teamsContacts";
    public static final String CREATE_TEAM = "Create Team";
    public static final String NEW_CONTACT = "New Contact";
    public static final String EDIT_CONTACT = "Edit Contact";
    public static final String PHONE_TRANSFER_CALL = "Glip_Mobile_phone_transferCall";

    /*Tasks Screen*/
    public static final String TASKS_TAB_SCREEN = "Tasks Tab";
    public static final String TASK_DETAILS_SCREEN = "Task Details";
    public static final String NEW_TASK_SCREEN = "New Task";
    public static final String EDIT_TASK_SCREEN = "Edit Task";

    /*Calendar Screen*/
    public static final String CALENDAR_TAB_SCREEN = "Calendar Tab";
    public static final String EVENT_DETAILS_SCREEN = "Event Details";
    public static final String CODE_SNIPPET_DETAILS_SCREEN = "Code Snippet Details";

    /*Search Screen*/
    public static final String SEARCH_SCREEN = "New Search";

    /*Profile Screen*/
    public static final String PROFILE_SCREEN = "Profile";
    public static final String GLIP_CONTACT_PROFILE = "Glip Contact Profile";
    public static final String DEVICE_CONTACT_PROFILE = "Device Contact Profile";
    public static final String UNKNOWN_CONTACT_PROFILE = "Unknown Contact Profile";
    public static final String CLOUD_CONTACT_PROFILE = "Cloud Contact Profile";
    public static final String PAGING_CONTACT_PROFILE = "Paging Contact Profile";
    public static final String SHARED_LINE_CONTACT_PROFILE = "Shared Line Contact Profile";
    public static final String MESSAGE_ONLY_CONTACT_PROFILE = "Message Only Contact Profile";
    public static final String ANNOUNCEMENT_ONLY_CONTACT_PROFILE = "Announcement Only Contact Profile";
    public static final String CALL_QUEUE_CONTACT_PROFILE = "Call Queue Contact Profile";
    public static final String LIMITED_CONTACT_PROFILE = "Limited Contact Profile";
    public static final String RC_COMPANY_CONTACT_PROFILE = "RC Company Contact Profile";
    public static final String GROUP_TEXT_PROFILE_SCREEN = "Glip_Mobile_Phone_groupSMSProfile";
    public static final String GUESTS_CONTACT_PROFILE_SCREEN = "Guests Contact Profile";
    public static final String GOOGLE_CONTACT_PROFILE_SCREEN = "Google Contact Profile";
    public static final String MICROSOFT_CONTACT_PROFILE_SCREEN = "Microsoft Contact Profile";

    /*Settings Screen*/
    public static final String SETTINGS_SCREEN = "Settings";
    public static final String WHATS_NEW_SCREEN = "What's New";
    public static final String ANSWER_CALLS = "Answer Calls";
    public static final String HELP_SCREEN = "Help";
    public static final String ADMIN_TOOLS = "Glip_Mobile_more_adminTools";

    /*Mixpanel*/
    public static final String CALL_QUEUE_MANAGEMENT = "Glip_Mobile_more_callQueueManagement";

    /*Gallery Screen*/

    public static final String SET_UP_YOUR_ACCOUNT = "Set Up Your Account";

    /*Browser*/
    public static final String BROWSER = "Browser";

    /*Event*/
    public static final String EDIT_EVENT_SCREEN = "Edit Event";
    public static final String NEW_EVENT_SCREEN = "New Event";

    public static final String EDIT_NOTE_SCREEN = "Edit Note";
    public static final String NEW_NOTE = "New Note";

    /*Phone*/
    public static final String PHONE_TAB = "Phone Tab";
    public static final String PHONE_CALLER_ID = "Phone Caller ID";
    public static final String ACTIVE_CALL = "Active Call";
    public static final String INCOMING_CALL = "Incoming Call";
    public static final String ACTIVE_CALL_KEYPAD = "Active Call Keypad";
    public static final String ACTIVE_CALL_TRANSFER = "Active Call Transfer";
    public static final String ACTIVE_CALL_FLIP = "Active Call Flip";
    public static final String PHONE_VOICEMAILS_SCREEN = "Phone Voicemails";
    public static final String PHONE_CALLS_SCREEN = "Glip_Mobile_phone_calls";
    public static final String PHONE_VOICEMAIL_DETAILS = "Voicemail Details";
    public static final String PHONE_TEXT_SCREEN = "Glip_Mobile_phone_text";
    public static final String PHONE_FAX_SCREEN = "Glip_Mobile_phone_fax";
    public static final String PHONE_FAX_DETAILS = "Phone Fax Details";
    public static final String PHONE_CALL_BACK_CALLER_ID = "Phone Call back Caller ID";

    /*My Profile*/
    public static final String MY_PROFILE = "My Profile";
    public static final String MOBILE_NOTIFICATIONS = "Mobile Notifications";
    public static final String MOBILE_NOTIFICATIONS_TEAMS = "Mobile Notifications Teams";
    public static final String EMAIL_NOTIFICATIONS = "Email Notifications";
    public static final String EMAIL_NOTIFICATIONS_DIRECT_MESSAGE = "Email Notifications Direct Message";
    public static final String EMAIL_NOTIFICATIONS_TEAMS = "Email Notifications Teams";
    public static final String CALL_SETTINGS_CALLER_ID = "Call Settings Caller ID";
    public static final String CALL_SETTINGS_REGION = "Call Settings Region";
    public static final String CALL_SETTINGS_REGION_COUNTRY = "Call Settings Region Country";
    public static final String CALL_SETTINGS_REGION_AREA_CODE = "Call Settings Region Area Code";
    public static final String CALL_SETTINGS_MY_ANDROID_NUMBER = "Call Settings My Android Number";

    public static final String NEW_SMS_SCREEN = "New SMS/MMS";
    public static final String SMS_CONVERSATION_SCREEN = "SMS/MMS Conversation";
    public static final String ABOUT_SEND_FEEDBACK_SCREEN = "About Send Feedback";

    public static final String TEAM_SETTINGS_SCREEN = "Team Settings";
    public static final String GROUP_SETTINGS_SCREEN = "Group Settings";
    public static final String TEAM_DESCRIPTION_SCREEN = "Team Description";
    public static final String TEAM_NAME_SCREEN = "Team Name";
    public static final String TEAM_TYPE_SCREEN = "Team Type";
    public static final String REMOVE_TEAM_MEMBERS_SCREEN = "Remove Team Members";
    public static final String UPDATE_TEAM_ADMINS_SCREEN = "Update Team Admins";
    public static final String CONVERT_TO_TEAM_SCREEN = "Convert To Team";
    public static final String JOIN_NOW = "Join Now";
    public static final String JOIN_NOW_SELECT_CALENDAR = "Join Now Select Calendar";
    public static final String JOIN_NOW_NOTIFICATION_SETTING = "Join Now Notification Setting";

    /*Meetings screen*/
    public static final String MEETINGS = "Meetings";
    public static final String RCV_MEETINGS_IN_MEETING_CHAT = "Glip_Mobile_rcv_inMeetingChat";
    public static final String RCV_MEETINGS_SEND_FEEDBACK = "Glip_Mobile_rcv_meetingsSendFeedback";
    public static final String RCV_MEETINGS_RECENTS_ALL = "Glip_Mobile_rcv_meetingsRecentsAll";
    public static final String RCV_MEETINGS_RECENTS_RECORDING = "Glip_Mobile_rcv_meetingsRecentsRecording";
    public static final String RCV_MEETINGS_UPCOMING_EVENT_LISTS = "Glip_Mobile_rcv_upcomingEventLists";
    public static final String CONVERT_TO_MEETING_SCREEN = "Glip_Mobile_meeting_addVideoMeeting";
    public static final String MEETING_DIAL_IN_COUNTRY_SCREEN = "Glip_Mobile_meeting_dail-InCountry";
    public static final String MEETING_DIAL_IN_COUNTRY_SELECTOR_SCREEN = "Glip_Mobile_meeting_addDial-InCountries";
    public static final String RCV_MEETING_DIAL_IN_SCREEN = "Glip_Mobile_meeting_dialInScreen";


    public static final String ATMENTION_SCREEN = "Glip_Mobile_appSettings_@mentions";

    /*bookmark list*/
    public static final String BOOKMARK_LIST_SCREEN = "Glip_Mobile_appSettings_bookmarks";

    /*pinned list*/
    public static final String PINNED_SCREEN = "Shelf Pinned";

    /*Flip to Glip*/
    public static final String FLIP_EMAIL_TO_GLIP = "Flip Email to Glip";

    /*Task Reply*/
    public static final String TASK_REPLY = "Task Replies";

    public static final String ACTIVE_MEETING = "Glip_Mobile_meeting_activeMeeting";
    public static final String MEETING_TAB = "Glip_Mobile_meeting_meetingTab";
    public static final String RCV_PARTICIPANT = "Glip_Mobile_rcv_participant";
    /**
     * Driving Model
     */
    public static final String RCV_DRIVING_MODE = "Glip_Mobile_meeting_drivingMode";
    /* Conference Call*/
    public static final String ADD_CALL = "Add Call";
    public static final String CONFERENCE_CALL_USER_LIST = "Conference Call User Lists";

    /*Fax*/
    public static final String CREATE_NEW_FAX = "New Fax";
    public static final String FAX_COVER_PAGE = "Fax Cover Page";

    /*Lab*/
    public static final String LABS_SCREEN = "Labs";

    public static final String RATE_APP_POP_UP = "Rate App Pop Up";

    public static final String MEETING_SETTINGS = "Glip_Mobile_meeting_meetingSettings";
    public static final String MEETING_INFO = "Glip_Mobile_meeting_meetingInfo";

    public static final String POST_MEETING_SIGN_UP = "Glip_Mobile_meeting_postMeetingSignup";
    /*Quick access*/
    public static final String QUICK_ACCESS_SCREEN ="Glip_Mobile_contacts_quickAccess";

    /*Company Call Log*/
    public static final String MORE_COMPANY_CALL_RECORDS = "Glip_Mobile_more_companyCallRecords";
    public static final String COMPANY_CALL_RECORDS_ALL = "Glip_Mobile_companyCallRecordsAll";
    public static final String COMPANY_CALL_RECORDS_MISSED_CALLS = "Glip_Mobile_companyCallRecordsMissedCalls";
    public static final String COMPANY_CALL_RECORDS_INBOUND_CALLS = "Glip_Mobile_companyCallRecordsInboundCalls";
    public static final String COMPANY_CALL_RECORDS_OUTBOUND_CALLS = "Glip_Mobile_companyCallRecordsOutboundCalls";
    public static final String COMPANY_CALL_RECORDS_SEND_FAXES = "Glip_Mobile_companyCallRecordsSendFaxes";
    public static final String COMPANY_CALL_RECORDS_RECEIVED_FAXES = "Glip_Mobile_companyCallRecordsReceivedFaxes";

    /*Text Message*/
    public static final String TEXT_CONVERSATION = "SMS/MMS Conversation";

    public static final String TWO_LEG_RINGOUT_ACTIVE_CALL = "Glip_Mobile_phone_2LegRingOutActiveCall";
    public static final String RINGOUT_NUMBER_SETTINGS = "Glip_Mobile_appSettings_ringOutNumbers";
    public static final String RINGOUT_NEW_NUMBER_SETTINGS = "Glip_Mobile_appSettings_ringOutNewNumber";

    /*Analytics*/
    public static final String ANALYTICS_SCREEN = "Glip_Mobile_more_analytics";

    /*RC Conference*/
    public static final String RC_CONFERENCE = "Glip_Mobile_phone_Conferencing";

    /*Share preview*/
    public static final String SHARE_PREVIEW = "share preview";

    public static String getScreenNameByUrl(Context context, String url) {
        if (url.equals(context.getString(R.string.sign_in_legal_terms_of_service_link))) {
            return ScreenName.TERMS_OF_SERVICE_SCREEN;
        } else if (url.equals(context.getString(R.string.sign_in_privacy_note_link))) {
            return ScreenName.PRIVACY_NOTE_SCREEN;
        } else if (url.equals(context.getString(R.string.sign_in_acceptable_use_policy_link))) {
            return ScreenName.ACCEPTABLE_USE_POLICY_SCREEN;
        } else {
            return ScreenName.BROWSER;
        }
    }
}
