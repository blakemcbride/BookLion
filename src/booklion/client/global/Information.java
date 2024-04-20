package booklion.client.global;


import booklion.client.supervisormain.SupervisorMain;

import java.util.LinkedList;

/**
 * @author Blake McBride
 * Date: 1/1/14
 */
public class Information {

    public static enum Type { CHAPTER, CHARACTER, LOCATION, SPECIES };

    private static booklion.client.booklist.Record book;

    private static String uuid;
    private static boolean isSupervisor;
    private static LinkedList<Long> validChapterIds;
    private static boolean limitChapters = false;
    private static long chapterLimitId;
    private static short chapterLimitSeqno;
    private static String chapterLimitDesignation;
    private static String chapterLimitName;
    private static long speciesId;
    private static String speciesName;
    private static long locationId;
    private static String locationName;
    private static long characterId;
    private static String characterFName;
    private static String characterLName;
    private static String characterSpeciesName;
    private static long chapterId;
    private static String chapterName;
    private static boolean isDirty = false;
    private static boolean guest = true;

    public static void setUser(String uu, String superVisor) {
        uuid = uu;
        isSupervisor = "Y".equals(superVisor);
    }

    public static String getUserUuid() {
        return uuid;
    }

    public static boolean isSupervisor() {
        return isSupervisor;
    }

    public static void setChapterLimit(long chapterId, short seqno, String chapterDesignation, String chapterName) {
        limitChapters = true;
        chapterLimitId = chapterId;
        chapterLimitSeqno = seqno;
        chapterLimitDesignation = chapterDesignation;
        chapterLimitName = chapterName;
    }

    public static void clearChapterLimit() {
        validChapterIds = null;
        limitChapters = false;
    }

    public static void setSpecies(long id, String name) {
        speciesId = id;
        speciesName = name;
    }

    public static long getSpeciesId() {
        return speciesId;
    }

    public static String getSpeciesName() {
        return speciesName;
    }

    public static void setLocation(long id, String name) {
        locationId = id;
        locationName = name;
    }

    public static long getLocationId() {
        return locationId;
    }

    public static String getLocationName() {
        return locationName;
    }

    public static void setCharacter(long id, String fname, String lname, String speciesName) {
        characterId = id;
        characterLName = lname;
        characterFName = fname;
        characterSpeciesName = speciesName;
    }

    public static long getCharacterId() {
        return characterId;
    }

    public static String getCharacterFLName() {
        String fname = characterFName;
        if (fname == null  ||  fname.isEmpty())
            return characterLName;
        else
            return characterFName + " " + characterLName;
    }

    public static String getCharacterLFName() {
        String fname = characterFName;
        if (fname == null  ||  fname.isEmpty())
            return characterLName;
        else
            return characterLName + ", " + characterFName;
    }

    public static String getCharacterSpeciesName() {
        return characterSpeciesName;
    }

    public static void setChapter(long id, String name) {
        chapterId = id;
        chapterName = name;
    }

    public static long getChapterId() {
        return chapterId;
    }

    public static String getChapterName() {
        return chapterName;
    }

    public static boolean isChapterLimit() {
        return limitChapters;
    }

    public static long getChapterLimitId() {
        return limitChapters ? chapterLimitId : -1;
    }

    public static short getChapterLimitSeqno() {
        return chapterLimitSeqno;
    }

    public static String getChapterLimitName() {
        if (limitChapters) {
            String fullName = chapterLimitDesignation != null  &&  !chapterLimitDesignation.isEmpty() ? chapterLimitDesignation : null;
            if (chapterLimitName != null  &&  !chapterLimitName.isEmpty())
                fullName = fullName == null ? chapterLimitName : fullName + " - " + chapterLimitName;
            return fullName;
        }
        return null;
    }

    public static booklion.client.booklist.Record getBook() {
        return book;
    }

    public static void setBook(booklion.client.booklist.Record bk) {
        book = bk;
    }

    public static void setValidChapterIds(LinkedList<Long> validChapterIds) {
        Information.validChapterIds = validChapterIds;
    }

    public static String getBookTitle() {
        return book == null ? null : book.getBookTitle();
    }

    public static boolean isDirty() {
        return isDirty;
    }

    public static void setDirty(boolean isDirty) {
        Information.isDirty = isDirty;
    }

    public static boolean isGuest() {
        return guest;
    }

    public static void setGuest(boolean isGuest) {
        Information.guest = isGuest;
    }

    /**
     * Is this one of the accepted chapters in a restricted chapter view?
     *
     * @param id
     * @return
     */
    public static boolean acceptChapterId(long id) {
        if (validChapterIds == null || validChapterIds.isEmpty()  ||  !limitChapters)
            return true;
        for (long tid : validChapterIds)
            if (tid == id)
                return true;
        return false;
    }

    public static void clearBookContext() {
        book = null;
        clearChapterLimit();
        SupervisorMain.setBookHeader();
    }

    public static void clearAll() {
        uuid = null;
        isSupervisor = false;
        clearBookContext();
    }
}
