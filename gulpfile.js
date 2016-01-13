var gulp = require('gulp')
var vulcanize = require('gulp-vulcanize')
var del = require('del')
var browserSync = require('browser-sync').create();

// Static server
gulp.task('brun', function() {
    browserSync.init({
        server: {
            baseDir: "app/"
        }
    });
});
gulp.task('clean', function () {
  return del([
    'dest/**/*'
  ])
})

gulp.task('vulcan', function () {
  return gulp.src('app/**/*.html')
    .pipe(vulcanize({
      abspath: '',
      excludes: [],
      inlineScripts: false,
      inlineCss: false,
      stripExcludes: false
    }))
    .pipe(gulp.dest('dest'))
})

gulp.task('css', function () {
  return gulp.src('app/**/*.css')
    .pipe(gulp.dest('dest'))
})

gulp.task('img', function () {
  return gulp.src(['app/**/*.png','app/**/*.jpg'])
    .pipe(gulp.dest('dest'))
})

gulp.task('js', function () {
  return gulp.src('app/**/*.js')
    .pipe(gulp.dest('dest'))
})

gulp.task('publish', function () {
  return gulp.src('dest/**/*')
    .pipe(gulp.dest('../mancereus.github.com'))
})

gulp.task('mdwatch', function () {
  gulp.watch('**/*.md', ['markdown'])
})

gulp.task('default', ['vulcan', 'img', 'js','css'], function() {
    gulp.start('publish');
});
