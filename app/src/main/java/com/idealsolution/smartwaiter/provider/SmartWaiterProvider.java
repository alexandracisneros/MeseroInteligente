package com.idealsolution.smartwaiter.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.MesaPiso;
import com.idealsolution.smartwaiter.database.SmartWaiterDatabase;
import com.idealsolution.smartwaiter.database.SmartWaiterDatabase.Tables;
import com.idealsolution.smartwaiter.util.SelectionBuilder;

import java.util.Arrays;

import static com.idealsolution.smartwaiter.util.LogUtils.LOGV;
import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;


public class SmartWaiterProvider extends ContentProvider {
    private static final String TAG = makeLogTag(SmartWaiterProvider.class);
    private SmartWaiterDatabase mDB;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //region ConstantsForURIs
    private static final int PEDIDO_CABECERAS = 100;
    private static final int PEDIDO_CABECERAS_ID = 101;

    private static final int PEDIDO_DETALLES = 200;
    private static final int PEDIDO_DETALLES_ID = 201;

    private static final int FAMILIAS = 300;
    private static final int FAMILIAS_ID = 301;

    private static final int PRIORIDADES = 400;
    private static final int PRIORIDADES_ID = 401;

    private static final int CLIENTES = 500;
    private static final int CLIENTES_ID = 501;

    private static final int MESA_PISOS = 600;
    private static final int MESA_PISOS_ID = 601;
    private static final int MESA_PISOS_PISOS = 602;
    private static final int MESA_PISOS_AMBIENTES = 603;

    private static final int CARTAS = 700;
    private static final int CARTAS_ID = 701;

    private static final int ARTICULOS = 800;
    private static final int ARTICULOS_ID = 801;
    //endregion

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SmartWaiterContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "pedido_cabeceras", PEDIDO_CABECERAS);
        matcher.addURI(authority, "pedido_cabeceras/#", PEDIDO_CABECERAS_ID);

        matcher.addURI(authority, "pedido_detalles", PEDIDO_DETALLES);
        matcher.addURI(authority, "pedido_detalles/#", PEDIDO_DETALLES_ID);

        matcher.addURI(authority, "familias", FAMILIAS);
        matcher.addURI(authority, "familias/#", FAMILIAS_ID);

        matcher.addURI(authority, "prioridades", PRIORIDADES);
        matcher.addURI(authority, "prioridades/#", PRIORIDADES_ID);

        matcher.addURI(authority, "clientes", CLIENTES);
        matcher.addURI(authority, "clientes/#", CLIENTES_ID);

        matcher.addURI(authority, "mesa_pisos", MESA_PISOS);
        matcher.addURI(authority, "mesa_pisos/#", MESA_PISOS_ID);
        matcher.addURI(authority, "mesa_pisos/pisos", MESA_PISOS_PISOS);
        matcher.addURI(authority, "mesa_pisos/ambientes", MESA_PISOS_AMBIENTES);

        matcher.addURI(authority, "cartas", CARTAS);
        matcher.addURI(authority, "cartas/#", CARTAS_ID);

        matcher.addURI(authority, "articulos", ARTICULOS);
        matcher.addURI(authority, "articulos/#", ARTICULOS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        // there should always be minimum operations inside
        // the onCreate as it runs on the main thread
        mDB = new SmartWaiterDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        //String tagsFilter = uri.getQueryParameter(Sessions.QUERY_PARAMETER_TAG_FILTER);
        final int match = sUriMatcher.match(uri);

        // avoid the expensive string concatenation below if not loggable
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            LOGV(TAG, "uri=" + uri + " match=" + match + " proj=" + Arrays.toString(projection) +
                    " selection=" + selection + " args=" + Arrays.toString(selectionArgs) + ")");
        }


        switch (match) {
            default: {
                // Most cases are handled with simple SelectionBuilder
                final SelectionBuilder builder = buildExpandedSelection(uri, match);

                // If a special filter was specified, try to apply it
//                if (!TextUtils.isEmpty(tagsFilter)) {
//                    addTagsFilter(builder, tagsFilter);
//                }

//                boolean distinct = !TextUtils.isEmpty(
//                        uri.getQueryParameter(SmartWaiterContract.QUERY_PARAMETER_DISTINCT));

                Cursor cursor = builder
                        .where(selection, selectionArgs)
                        .query(mDB, projection, sortOrder, null);

                Context context = getContext();
                if (null != context) {
                    cursor.setNotificationUri(context.getContentResolver(), uri);
                }
                return cursor;
            }
//            case SEARCH_SUGGEST: {
//                final SelectionBuilder builder = new SelectionBuilder();
//
//                // Adjust incoming query to become SQL text match
//                selectionArgs[0] = selectionArgs[0] + "%";
//                builder.table(Tables.SEARCH_SUGGEST);
//                builder.where(selection, selectionArgs);
//                builder.map(SearchManager.SUGGEST_COLUMN_QUERY,
//                        SearchManager.SUGGEST_COLUMN_TEXT_1);
//
//                projection = new String[] {
//                        BaseColumns._ID,
//                        SearchManager.SUGGEST_COLUMN_TEXT_1,
//                        SearchManager.SUGGEST_COLUMN_QUERY
//                };
//
//                final String limit = uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT);
//                return builder.query(db, false, projection, SearchSuggest.DEFAULT_SORT, limit);
//            }
        }
    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
            case FAMILIAS: {
                return builder.table(Tables.FAMILIA);
            }
            case MESA_PISOS: {
                return builder.table(Tables.MESA_PISO);
            }
            case MESA_PISOS_PISOS: {
                return builder.table(Tables.MESA_PISO);
            }
            case MESA_PISOS_AMBIENTES: {
                return builder.table(Tables.MESA_PISO);
            }
//            case BLOCKS_BETWEEN: {
//                final List<String> segments = uri.getPathSegments();
//                final String startTime = segments.get(2);
//                final String endTime = segments.get(3);
//                return builder.table(Tables.BLOCKS)
//                        .where(Blocks.BLOCK_START + ">=?", startTime)
//                        .where(Blocks.BLOCK_START + "<=?", endTime);
//            }
//            case BLOCKS_ID: {
//                final String blockId = Blocks.getBlockId(uri);
//                return builder.table(Tables.BLOCKS)
//                        .where(Blocks.BLOCK_ID + "=?", blockId);
//            }
//            case TAGS: {
//                return builder.table(Tables.TAGS);
//            }
//            case TAGS_ID: {
//                final String tagId = Tags.getTagId(uri);
//                return builder.table(Tables.TAGS)
//                        .where(Tags.TAG_ID + "=?", tagId);
//            }
//            case ROOMS: {
//                return builder.table(Tables.ROOMS);
//            }
//            case ROOMS_ID: {
//                final String roomId = Rooms.getRoomId(uri);
//                return builder.table(Tables.ROOMS)
//                        .where(Rooms.ROOM_ID + "=?", roomId);
//            }
//            case ROOMS_ID_SESSIONS: {
//                final String roomId = Rooms.getRoomId(uri);
//                return builder.table(Tables.SESSIONS_JOIN_ROOMS)
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .where(Qualified.SESSIONS_ROOM_ID + "=?", roomId);
//            }
//            case SESSIONS: {
//                // We query sessions on the joined table of sessions with rooms and tags.
//                // Since there may be more than one tag per session, we GROUP BY session ID.
//                // The starred sessions ("my schedule") are associated with a user, so we
//                // use the current user to select them properly
//                return builder.table(Tables.SESSIONS_JOIN_ROOMS_TAGS, getCurrentAccountName(uri, true))
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.SESSION_ID, Tables.SESSIONS)
//                        .map(Sessions.SESSION_IN_MY_SCHEDULE, "IFNULL(in_schedule, 0)")
//                        .groupBy(Qualified.SESSIONS_SESSION_ID);
//            }
//            case SESSIONS_COUNTER: {
//                return builder.table(Tables.SESSIONS_JOIN_MYSCHEDULE, getCurrentAccountName(uri, true))
//                        .map(Sessions.SESSION_INTERVAL_COUNT, "count(1)")
//                        .map(Sessions.SESSION_IN_MY_SCHEDULE, "IFNULL(in_schedule, 0)")
//                        .groupBy(Sessions.SESSION_START + ", " + Sessions.SESSION_END);
//            }
//            case SESSIONS_MY_SCHEDULE: {
//                return builder.table(Tables.SESSIONS_JOIN_ROOMS_TAGS_FEEDBACK_MYSCHEDULE, getCurrentAccountName(uri, true))
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.SESSION_ID, Tables.SESSIONS)
//                        .map(Sessions.HAS_GIVEN_FEEDBACK, Subquery.SESSION_HAS_GIVEN_FEEDBACK)
//                        .map(Sessions.SESSION_IN_MY_SCHEDULE, "IFNULL(in_schedule, 0)")
//                        .where("( " + Sessions.SESSION_IN_MY_SCHEDULE + "=1 OR " +
//                                Sessions.SESSION_TAGS +
//                                " LIKE '%" + Config.Tags.SPECIAL_KEYNOTE + "%' )")
//                        .groupBy(Qualified.SESSIONS_SESSION_ID);
//            }
//            case SESSIONS_UNSCHEDULED: {
//                final long[] interval = Sessions.getInterval(uri);
//                return builder.table(Tables.SESSIONS_JOIN_ROOMS_TAGS_FEEDBACK_MYSCHEDULE, getCurrentAccountName(uri, true))
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.SESSION_ID, Tables.SESSIONS)
//                        .map(Sessions.SESSION_IN_MY_SCHEDULE, "IFNULL(in_schedule, 0)")
//                        .where(Sessions.SESSION_IN_MY_SCHEDULE + "=0")
//                        .where(Sessions.SESSION_START + ">=?", String.valueOf(interval[0]))
//                        .where(Sessions.SESSION_START + "<?", String.valueOf(interval[1]))
//                        .groupBy(Qualified.SESSIONS_SESSION_ID);
//            }
//            case SESSIONS_SEARCH: {
//                final String query = Sessions.getSearchQuery(uri);
//                return builder.table(Tables.SESSIONS_SEARCH_JOIN_SESSIONS_ROOMS, getCurrentAccountName(uri, true))
//                        .map(Sessions.SEARCH_SNIPPET, Subquery.SESSIONS_SNIPPET)
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.SESSION_ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .map(Sessions.SESSION_IN_MY_SCHEDULE, "IFNULL(in_schedule, 0)")
//                        .where(SessionsSearchColumns.BODY + " MATCH ?", query);
//            }
//            case SESSIONS_AT: {
//                final List<String> segments = uri.getPathSegments();
//                final String time = segments.get(2);
//                return builder.table(Tables.SESSIONS_JOIN_ROOMS, getCurrentAccountName(uri, true))
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .where(Sessions.SESSION_START + "<=?", time)
//                        .where(Sessions.SESSION_END + ">=?", time);
//            }
//            case SESSIONS_ID: {
//                final String sessionId = Sessions.getSessionId(uri);
//                return builder.table(Tables.SESSIONS_JOIN_ROOMS, getCurrentAccountName(uri, true))
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.SESSION_ID, Tables.SESSIONS)
//                        .map(Sessions.SESSION_IN_MY_SCHEDULE, "IFNULL(in_schedule, 0)")
//                        .where(Qualified.SESSIONS_SESSION_ID + "=?", sessionId);
//            }
//            case SESSIONS_ID_SPEAKERS: {
//                final String sessionId = Sessions.getSessionId(uri);
//                return builder.table(Tables.SESSIONS_SPEAKERS_JOIN_SPEAKERS)
//                        .mapToTable(Speakers._ID, Tables.SPEAKERS)
//                        .mapToTable(Speakers.SPEAKER_ID, Tables.SPEAKERS)
//                        .where(Qualified.SESSIONS_SPEAKERS_SESSION_ID + "=?", sessionId);
//            }
//            case SESSIONS_ID_TAGS: {
//                final String sessionId = Sessions.getSessionId(uri);
//                return builder.table(Tables.SESSIONS_TAGS_JOIN_TAGS)
//                        .mapToTable(Tags._ID, Tables.TAGS)
//                        .mapToTable(Tags.TAG_ID, Tables.TAGS)
//                        .where(Qualified.SESSIONS_TAGS_SESSION_ID + "=?", sessionId);
//            }
//            case SESSIONS_ROOM_AFTER: {
//                final String room = Sessions.getRoom(uri);
//                final String time = Sessions.getAfter(uri);
//                return builder.table(Tables.SESSIONS_JOIN_ROOMS, getCurrentAccountName(uri, true))
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .where(Qualified.SESSIONS_ROOM_ID+ "=?", room)
//                        .where("("+Sessions.SESSION_START + "<= ? AND "+Sessions.SESSION_END+
//                                " >= ?) OR ("+Sessions.SESSION_START + " >= ?)", time,time,time);
//            }
//            case SPEAKERS: {
//                return builder.table(Tables.SPEAKERS);
//            }
//            case MY_SCHEDULE: {
//                // force a where condition to avoid leaking schedule info to another account
//                // Note that, since SelectionBuilder always join multiple where calls using AND,
//                // even if malicious code specifying additional conditions on account_name won't
//                // be able to fetch data from a different account.
//                return builder.table(Tables.MY_SCHEDULE)
//                        .where(MySchedule.MY_SCHEDULE_ACCOUNT_NAME + "=?", getCurrentAccountName(uri, true));
//            }
//            case SPEAKERS_ID: {
//                final String speakerId = Speakers.getSpeakerId(uri);
//                return builder.table(Tables.SPEAKERS)
//                        .where(Speakers.SPEAKER_ID + "=?", speakerId);
//            }
//            case SPEAKERS_ID_SESSIONS: {
//                final String speakerId = Speakers.getSpeakerId(uri);
//                return builder.table(Tables.SESSIONS_SPEAKERS_JOIN_SESSIONS_ROOMS)
//                        .mapToTable(Sessions._ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.SESSION_ID, Tables.SESSIONS)
//                        .mapToTable(Sessions.ROOM_ID, Tables.SESSIONS)
//                        .where(Qualified.SESSIONS_SPEAKERS_SPEAKER_ID + "=?", speakerId);
//            }
//            case ANNOUNCEMENTS: {
//                return builder.table(Tables.ANNOUNCEMENTS);
//            }
//            case ANNOUNCEMENTS_ID: {
//                final String announcementId = Announcements.getAnnouncementId(uri);
//                return builder.table(Tables.ANNOUNCEMENTS)
//                        .where(Announcements.ANNOUNCEMENT_ID + "=?", announcementId);
//            }
//            case MAPMARKERS: {
//                return builder.table(Tables.MAPMARKERS);
//            }
//            case MAPMARKERS_FLOOR: {
//                final String floor = MapMarkers.getMarkerFloor(uri);
//                return builder.table(Tables.MAPMARKERS)
//                        .where(MapMarkers.MARKER_FLOOR + "=?", floor);
//            }
//            case MAPMARKERS_ID: {
//                final String roomId = MapMarkers.getMarkerId(uri);
//                return builder.table(Tables.MAPMARKERS)
//                        .where(MapMarkers.MARKER_ID + "=?", roomId);
//            }
//            case MAPTILES: {
//                return builder.table(Tables.MAPTILES);
//            }
//            case MAPTILES_FLOOR: {
//                final String floor = MapTiles.getFloorId(uri);
//                return builder.table(Tables.MAPTILES)
//                        .where(MapTiles.TILE_FLOOR + "=?", floor);
//            }
//            case FEEDBACK_FOR_SESSION: {
//                final String sessionId = Feedback.getSessionId(uri);
//                return builder.table(Tables.FEEDBACK)
//
//                        .where(Feedback.SESSION_ID + "=?", sessionId);
//            }
//            case FEEDBACK_ALL: {
//                return builder.table(Tables.FEEDBACK);
//            }
//            case EXPERTS: {
//                return builder.table(Tables.EXPERTS);
//            }
//            case EXPERTS_ID: {
//                String expertId = Experts.getExpertId(uri);
//                return builder.table(Tables.EXPERTS)
//                        .where(Experts.EXPERT_ID + "= ?", expertId);
//            }
//            case HASHTAGS: {
//                return builder.table(Tables.HASHTAGS);
//            }
//            case HASHTAGS_NAME: {
//                final String hashtagName = Hashtags.getHashtagName(uri);
//                return builder.table(Tables.HASHTAGS)
//                        .where(HashtagColumns.HASHTAG_NAME + "=?", hashtagName);
//            }
//            case PEOPLE_IVE_MET: {
//                return builder.table(Tables.PEOPLE_IVE_MET);
//            }
//            case PEOPLE_IVE_MET_ID: {
//                String personId = ScheduleContract.PeopleIveMet.getPersonId(uri);
//                return builder.table(Tables.PEOPLE_IVE_MET)
//                        .where(PeopleIveMet.PERSON_ID + "=?", personId);
//            }
//            case VIDEOS: {
//                return builder.table(Tables.VIDEOS);
//            }
//            case VIDEOS_ID: {
//                final String videoId = Videos.getVideoId(uri);
//                return builder.table(Tables.VIDEOS)
//                        .where(VideoColumns.VIDEO_ID + "=?", videoId);
//            }
//            case PARTNERS: {
//                return builder.table(Tables.PARTNERS);
//            }
//            case PARTNERS_ID: {
//                final String partnerId = Partners.getPartnerId(uri);
//                return builder.table(Tables.PARTNERS).where(Partners.PARTNER_ID + "=?", partnerId);
//            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            //Mesa Piso - Inicio
            case MESA_PISOS:
                return MesaPiso.CONTENT_TYPE;
            case MESA_PISOS_ID:
                return MesaPiso.CONTENT_ITEM_TYPE;
            case MESA_PISOS_PISOS:
                return MesaPiso.CONTENT_TYPE;
            case MESA_PISOS_AMBIENTES:
                return MesaPiso.CONTENT_TYPE;
            //Mesa Piso - Fin
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        Log.d(SmartWaiterContract.TAG, "LLegue a bulkInsert del ContentProvider. URL:" + uri.toString());
        int numInserted;
        Log.d(SmartWaiterContract.TAG, "LLegue a match=" + match);
        switch (match) {
            case FAMILIAS: {
                numInserted = mDB.InsertFamilias(values);
                return numInserted;
            }
            case PRIORIDADES: {
                numInserted = mDB.InsertPrioridades(values);
                return numInserted;
            }
            case CLIENTES: {
                numInserted = mDB.InsertClientes(values);
                return numInserted;
            }
            case MESA_PISOS: {
                numInserted = mDB.InsertMesaPisos(values);
                return numInserted;
            }
            case CARTAS: {
                numInserted = mDB.InsertCarta(values);
                return numInserted;
            }
            case ARTICULOS: {
                numInserted = mDB.InsertArticulos(values);
                return numInserted;
            }
            default: {
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);
            }
        }
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAMILIAS: {
                return builder.table(Tables.FAMILIA);
            }
            case PRIORIDADES: {
//                final String blockId = Blocks.getBlockId(uri);
//                return builder.table(SmartWaiterDatabase.Tables.BLOCKS)
//                        .where(Blocks.BLOCK_ID + "=?", blockId);
                return builder.table(Tables.PRIORIDAD);
            }
            case CLIENTES: {
                return builder.table(Tables.CLIENTE);
            }
            case MESA_PISOS: {
//                final String tagId = Tags.getTagId(uri);
//                return builder.table(Tables.TAGS)
//                        .where(Tags.TAG_ID + "=?", tagId);
                return builder.table(Tables.MESA_PISO);
            }
            case MESA_PISOS_PISOS: {
                return builder.table(Tables.MESA_PISO);
            }
            case MESA_PISOS_AMBIENTES: {
                return builder.table(Tables.MESA_PISO);
            }
            case CARTAS: {
//                return builder.table(Tables.ROOMS);
                return builder.table(Tables.CARTA);
            }
            case ARTICULOS: {
//                final String roomId = Rooms.getRoomId(uri);
//                return builder.table(Tables.ROOMS)
//                        .where(Rooms.ROOM_ID + "=?", roomId);
                return builder.table(Tables.ARTICULO);
            }
//            case SESSIONS: {
//                return builder.table(Tables.SESSIONS);
//            }
//            case SESSIONS_ID: {
//                final String sessionId = Sessions.getSessionId(uri);
//                return builder.table(Tables.SESSIONS)
//                        .where(Sessions.SESSION_ID + "=?", sessionId);
//            }
//            case SESSIONS_ID_SPEAKERS: {
//                final String sessionId = Sessions.getSessionId(uri);
//                return builder.table(Tables.SESSIONS_SPEAKERS)
//                        .where(Sessions.SESSION_ID + "=?", sessionId);
//            }
//            case SESSIONS_ID_TAGS: {
//                final String sessionId = Sessions.getSessionId(uri);
//                return builder.table(Tables.SESSIONS_TAGS)
//                        .where(Sessions.SESSION_ID + "=?", sessionId);
//            }
//            case SESSIONS_MY_SCHEDULE: {
//                final String sessionId = Sessions.getSessionId(uri);
//                return builder.table(Tables.MY_SCHEDULE)
//                        .where(ScheduleContract.MyScheduleColumns.SESSION_ID + "=?", sessionId);
//            }
//            case MY_SCHEDULE: {
//                return builder.table(Tables.MY_SCHEDULE)
//                        .where(MySchedule.MY_SCHEDULE_ACCOUNT_NAME + "=?", getCurrentAccountName(uri, false));
//            }
//            case SPEAKERS: {
//                return builder.table(Tables.SPEAKERS);
//            }
//            case SPEAKERS_ID: {
//                final String speakerId = Speakers.getSpeakerId(uri);
//                return builder.table(Tables.SPEAKERS)
//                        .where(Speakers.SPEAKER_ID + "=?", speakerId);
//            }
//            case ANNOUNCEMENTS: {
//                return builder.table(Tables.ANNOUNCEMENTS);
//            }
//            case ANNOUNCEMENTS_ID: {
//                final String announcementId = Announcements.getAnnouncementId(uri);
//                return builder.table(Tables.ANNOUNCEMENTS)
//                        .where(Announcements.ANNOUNCEMENT_ID + "=?", announcementId);
//            }
//            case MAPMARKERS: {
//                return builder.table(Tables.MAPMARKERS);
//            }
//            case MAPMARKERS_FLOOR: {
//                final String floor = MapMarkers.getMarkerFloor(uri);
//                return builder.table(Tables.MAPMARKERS)
//                        .where(MapMarkers.MARKER_FLOOR+ "=?", floor);
//            }
//            case MAPMARKERS_ID: {
//                final String markerId = MapMarkers.getMarkerId(uri);
//                return builder.table(Tables.MAPMARKERS)
//                        .where(MapMarkers.MARKER_ID + "=?", markerId);
//            }
//            case MAPTILES: {
//                return builder.table(Tables.MAPTILES);
//            }
//            case MAPTILES_FLOOR: {
//                final String  floor = MapTiles.getFloorId(uri);
//                return builder.table(Tables.MAPTILES)
//                        .where(MapTiles.TILE_FLOOR+ "=?", floor);
//            }
//            case SEARCH_SUGGEST: {
//                return builder.table(Tables.SEARCH_SUGGEST);
//            }
//            case FEEDBACK_FOR_SESSION: {
//                final String session_id  = Feedback.getSessionId(uri);
//                return builder.table(Tables.FEEDBACK)
//                        .where(Feedback.SESSION_ID + "=?", session_id);
//            }
//            case FEEDBACK_ALL: {
//                return builder.table(Tables.FEEDBACK);
//            }
//            case EXPERTS: {
//                return builder.table(Tables.EXPERTS);
//            }
//            case EXPERTS_ID: {
//                String expertId = Experts.getExpertId(uri);
//                return builder.table(Tables.EXPERTS)
//                        .where(Experts.EXPERT_ID + "= ?", expertId);
//            }
//            case HASHTAGS: {
//                return builder.table(Tables.HASHTAGS);
//            }
//            case HASHTAGS_NAME: {
//                final String hashtagName = Hashtags.getHashtagName(uri);
//                return builder.table(Tables.HASHTAGS)
//                        .where(Hashtags.HASHTAG_NAME + "=?", hashtagName);
//            }
//            case PEOPLE_IVE_MET: {
//                return builder.table(Tables.PEOPLE_IVE_MET);
//            }
//            case PEOPLE_IVE_MET_ID: {
//                String personId = ScheduleContract.PeopleIveMet.getPersonId(uri);
//                return builder.table(Tables.PEOPLE_IVE_MET)
//                        .where(PeopleIveMet.PERSON_ID + "=?", personId);
//            }
//            case VIDEOS: {
//                return builder.table(Tables.VIDEOS);
//            }
//            case VIDEOS_ID: {
//                final String videoId = Videos.getVideoId(uri);
//                return builder.table(Tables.VIDEOS).where(Videos.VIDEO_ID + "=?", videoId);
//            }
//            case PARTNERS: {
//                return builder.table(Tables.PARTNERS);
//            }
//            case PARTNERS_ID: {
//                final String partnerId = Partners.getPartnerId(uri);
//                return builder.table(Tables.PARTNERS).where(Partners.PARTNER_ID + "=?", partnerId);
//            }
            default: {
                throw new UnsupportedOperationException("Unknown uri for " + match + ": " + uri);
            }
        }
    }
}
