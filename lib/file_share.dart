import 'dart:async';

import 'package:flutter/services.dart';

class FileShare {
  static const MethodChannel _channel =
      const MethodChannel('github.com/dhayaec/file_share');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<void> filePath(
    String title,
    String filePath,
  ) async {
    Map argsMap = <String, String>{'title': '$title', 'filePath': '$filePath'};

    _channel.invokeMethod('filePath', argsMap);
  }

  static Future<void> shareMultiple(List<String> videoPaths){
    Map argsMap = <String, String>{ 'videoPaths': '$videoPaths'};

    _channel.invokeMethod('shareMultiple', argsMap);
  }
}
