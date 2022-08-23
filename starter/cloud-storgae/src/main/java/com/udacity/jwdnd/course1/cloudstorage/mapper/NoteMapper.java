package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE notetitle = #{notetitle} AND notedescription = #{notedescription}")
    Note getNote(@Param("notetitle") String notetitle, @Param("notedescription") String notedescription);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid}")
    Note getNoteById(@Param("noteid") Integer noteid);

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    List<Note> getUserNotes(@Param("userid") Integer userid);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES " +
            "(#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    void save(Note note);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid} AND userid = #{userid}")
    void update(@Param("noteid") Integer noteid, @Param("notetitle") String notetitle, @Param("notedescription") String notedescription, @Param("userid") Integer userid);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid} AND userid = #{userid}")
    void delete(@Param("noteid") Integer noteid, @Param("userid") Integer userid);

}
