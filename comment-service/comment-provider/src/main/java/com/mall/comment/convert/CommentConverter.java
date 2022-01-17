package com.mall.comment.convert;

import com.mall.comment.dal.entitys.Comment;
import com.mall.comment.dal.entitys.CommentReply;
import com.mall.comment.dto.CommentDto;
import com.mall.comment.dto.CommentReplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @author heps
 * @date 2019/8/14 23:54
 */
@Mapper(componentModel = "spring")
public interface CommentConverter {

    @Mappings({})
    CommentDto comment2Dto(Comment comment);

    List<CommentDto> comment2Dto(List<Comment> commentList);

    CommentReplyDto commentReply2Dto(CommentReply commentReply);

    List<CommentReplyDto> commentReply2Dto(List<CommentReply> commentReplyList);
}
