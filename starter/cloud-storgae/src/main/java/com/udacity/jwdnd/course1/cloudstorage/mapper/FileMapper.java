package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    File getFileByName(@Param("filename") String filename);

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> getFilesFromUser(@Param("userid") Integer userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES "
            + "(#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void save(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId} AND userid = #{userid}")
    void delete(@Param("fileId") Integer fileId, @Param("userid") Integer userid);

}
