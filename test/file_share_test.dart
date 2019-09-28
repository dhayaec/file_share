import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:file_share/file_share.dart';

void main() {
  const MethodChannel channel = MethodChannel('file_share');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FileShare.platformVersion, '42');
  });
}
