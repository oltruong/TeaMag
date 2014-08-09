// Copyright (c) 2014, the Dart project authors.  Please see the AUTHORS file
// for details. All rights reserved. Use of this source code is governed by a
// BSD-style license that can be found in the LICENSE file.

library teamag.main;

import 'package:angular/angular.dart';
import 'package:teamag/teamag_module.dart';

@MirrorsUsed(override: '*')
import 'dart:mirrors';

void main() {
  ngBootstrap(module: new TeamagModule());
}