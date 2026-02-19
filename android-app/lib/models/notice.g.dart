part of 'notice.dart';

Notice _$NoticeFromJson(Map<String, dynamic> json) => Notice(
      id: (json['id'] as num).toInt(),
      title: json['title'] as String,
      content: json['content'] as String,
      status: json['status'] as String?,
      targetAudience: json['targetAudience'] as String?,
      authorId: (json['authorId'] as num?)?.toInt(),
      createdAt: json['createdAt'] as String?,
      updatedAt: json['updatedAt'] as String?,
    );

Map<String, dynamic> _$NoticeToJson(Notice instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'content': instance.content,
      'status': instance.status,
      'targetAudience': instance.targetAudience,
      'authorId': instance.authorId,
      'createdAt': instance.createdAt,
      'updatedAt': instance.updatedAt,
    };
