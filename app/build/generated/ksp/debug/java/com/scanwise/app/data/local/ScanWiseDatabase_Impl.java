package com.scanwise.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScanWiseDatabase_Impl extends ScanWiseDatabase {
  private volatile ScanHistoryDao _scanHistoryDao;

  private volatile BlacklistedUrlDao _blacklistedUrlDao;

  private volatile MaliciousPatternDao _maliciousPatternDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `scan_history` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `qrUrl` TEXT NOT NULL, `domainName` TEXT NOT NULL, `riskScore` REAL NOT NULL, `riskLevel` TEXT NOT NULL, `analysisDetailsJson` TEXT NOT NULL, `scanTimestamp` INTEGER NOT NULL, `threatDetected` INTEGER NOT NULL, `userAction` TEXT, `actionTimestamp` INTEGER)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_scan_history_scanTimestamp` ON `scan_history` (`scanTimestamp`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_scan_history_riskLevel` ON `scan_history` (`riskLevel`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `blacklisted_urls` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `url` TEXT NOT NULL, `domain` TEXT NOT NULL, `threatType` TEXT, `severity` INTEGER NOT NULL, `source` TEXT NOT NULL, `lastUpdated` INTEGER NOT NULL, `detectionCount` INTEGER NOT NULL, `isArchived` INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_blacklisted_urls_domain` ON `blacklisted_urls` (`domain`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_blacklisted_urls_threatType` ON `blacklisted_urls` (`threatType`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `malicious_patterns` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pattern` TEXT NOT NULL, `patternType` TEXT NOT NULL, `threatLevel` TEXT NOT NULL, `description` TEXT, `isActive` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c85e18946a5bf73bc23778a32fb6e0af')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `scan_history`");
        db.execSQL("DROP TABLE IF EXISTS `blacklisted_urls`");
        db.execSQL("DROP TABLE IF EXISTS `malicious_patterns`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsScanHistory = new HashMap<String, TableInfo.Column>(10);
        _columnsScanHistory.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("qrUrl", new TableInfo.Column("qrUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("domainName", new TableInfo.Column("domainName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("riskScore", new TableInfo.Column("riskScore", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("riskLevel", new TableInfo.Column("riskLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("analysisDetailsJson", new TableInfo.Column("analysisDetailsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("scanTimestamp", new TableInfo.Column("scanTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("threatDetected", new TableInfo.Column("threatDetected", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("userAction", new TableInfo.Column("userAction", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanHistory.put("actionTimestamp", new TableInfo.Column("actionTimestamp", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScanHistory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScanHistory = new HashSet<TableInfo.Index>(2);
        _indicesScanHistory.add(new TableInfo.Index("index_scan_history_scanTimestamp", false, Arrays.asList("scanTimestamp"), Arrays.asList("ASC")));
        _indicesScanHistory.add(new TableInfo.Index("index_scan_history_riskLevel", false, Arrays.asList("riskLevel"), Arrays.asList("ASC")));
        final TableInfo _infoScanHistory = new TableInfo("scan_history", _columnsScanHistory, _foreignKeysScanHistory, _indicesScanHistory);
        final TableInfo _existingScanHistory = TableInfo.read(db, "scan_history");
        if (!_infoScanHistory.equals(_existingScanHistory)) {
          return new RoomOpenHelper.ValidationResult(false, "scan_history(com.scanwise.app.data.local.ScanHistoryEntity).\n"
                  + " Expected:\n" + _infoScanHistory + "\n"
                  + " Found:\n" + _existingScanHistory);
        }
        final HashMap<String, TableInfo.Column> _columnsBlacklistedUrls = new HashMap<String, TableInfo.Column>(9);
        _columnsBlacklistedUrls.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlacklistedUrls.put("url", new TableInfo.Column("url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlacklistedUrls.put("domain", new TableInfo.Column("domain", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlacklistedUrls.put("threatType", new TableInfo.Column("threatType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlacklistedUrls.put("severity", new TableInfo.Column("severity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlacklistedUrls.put("source", new TableInfo.Column("source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlacklistedUrls.put("lastUpdated", new TableInfo.Column("lastUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlacklistedUrls.put("detectionCount", new TableInfo.Column("detectionCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBlacklistedUrls.put("isArchived", new TableInfo.Column("isArchived", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBlacklistedUrls = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBlacklistedUrls = new HashSet<TableInfo.Index>(2);
        _indicesBlacklistedUrls.add(new TableInfo.Index("index_blacklisted_urls_domain", false, Arrays.asList("domain"), Arrays.asList("ASC")));
        _indicesBlacklistedUrls.add(new TableInfo.Index("index_blacklisted_urls_threatType", false, Arrays.asList("threatType"), Arrays.asList("ASC")));
        final TableInfo _infoBlacklistedUrls = new TableInfo("blacklisted_urls", _columnsBlacklistedUrls, _foreignKeysBlacklistedUrls, _indicesBlacklistedUrls);
        final TableInfo _existingBlacklistedUrls = TableInfo.read(db, "blacklisted_urls");
        if (!_infoBlacklistedUrls.equals(_existingBlacklistedUrls)) {
          return new RoomOpenHelper.ValidationResult(false, "blacklisted_urls(com.scanwise.app.data.local.BlacklistedUrlEntity).\n"
                  + " Expected:\n" + _infoBlacklistedUrls + "\n"
                  + " Found:\n" + _existingBlacklistedUrls);
        }
        final HashMap<String, TableInfo.Column> _columnsMaliciousPatterns = new HashMap<String, TableInfo.Column>(6);
        _columnsMaliciousPatterns.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaliciousPatterns.put("pattern", new TableInfo.Column("pattern", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaliciousPatterns.put("patternType", new TableInfo.Column("patternType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaliciousPatterns.put("threatLevel", new TableInfo.Column("threatLevel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaliciousPatterns.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMaliciousPatterns.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMaliciousPatterns = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMaliciousPatterns = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMaliciousPatterns = new TableInfo("malicious_patterns", _columnsMaliciousPatterns, _foreignKeysMaliciousPatterns, _indicesMaliciousPatterns);
        final TableInfo _existingMaliciousPatterns = TableInfo.read(db, "malicious_patterns");
        if (!_infoMaliciousPatterns.equals(_existingMaliciousPatterns)) {
          return new RoomOpenHelper.ValidationResult(false, "malicious_patterns(com.scanwise.app.data.local.MaliciousPatternEntity).\n"
                  + " Expected:\n" + _infoMaliciousPatterns + "\n"
                  + " Found:\n" + _existingMaliciousPatterns);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c85e18946a5bf73bc23778a32fb6e0af", "370fd2afefae87bd65002d2f05fa0d8c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "scan_history","blacklisted_urls","malicious_patterns");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `scan_history`");
      _db.execSQL("DELETE FROM `blacklisted_urls`");
      _db.execSQL("DELETE FROM `malicious_patterns`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ScanHistoryDao.class, ScanHistoryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BlacklistedUrlDao.class, BlacklistedUrlDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MaliciousPatternDao.class, MaliciousPatternDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ScanHistoryDao scanHistoryDao() {
    if (_scanHistoryDao != null) {
      return _scanHistoryDao;
    } else {
      synchronized(this) {
        if(_scanHistoryDao == null) {
          _scanHistoryDao = new ScanHistoryDao_Impl(this);
        }
        return _scanHistoryDao;
      }
    }
  }

  @Override
  public BlacklistedUrlDao blacklistedUrlDao() {
    if (_blacklistedUrlDao != null) {
      return _blacklistedUrlDao;
    } else {
      synchronized(this) {
        if(_blacklistedUrlDao == null) {
          _blacklistedUrlDao = new BlacklistedUrlDao_Impl(this);
        }
        return _blacklistedUrlDao;
      }
    }
  }

  @Override
  public MaliciousPatternDao maliciousPatternDao() {
    if (_maliciousPatternDao != null) {
      return _maliciousPatternDao;
    } else {
      synchronized(this) {
        if(_maliciousPatternDao == null) {
          _maliciousPatternDao = new MaliciousPatternDao_Impl(this);
        }
        return _maliciousPatternDao;
      }
    }
  }
}
