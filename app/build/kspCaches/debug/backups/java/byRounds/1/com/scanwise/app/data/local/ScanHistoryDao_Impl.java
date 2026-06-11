package com.scanwise.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScanHistoryDao_Impl implements ScanHistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScanHistoryEntity> __insertionAdapterOfScanHistoryEntity;

  private final EntityDeletionOrUpdateAdapter<ScanHistoryEntity> __deletionAdapterOfScanHistoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  public ScanHistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScanHistoryEntity = new EntityInsertionAdapter<ScanHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `scan_history` (`id`,`qrUrl`,`domainName`,`riskScore`,`riskLevel`,`analysisDetailsJson`,`scanTimestamp`,`threatDetected`,`userAction`,`actionTimestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScanHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getQrUrl());
        statement.bindString(3, entity.getDomainName());
        statement.bindDouble(4, entity.getRiskScore());
        statement.bindString(5, entity.getRiskLevel());
        statement.bindString(6, entity.getAnalysisDetailsJson());
        statement.bindLong(7, entity.getScanTimestamp());
        final int _tmp = entity.getThreatDetected() ? 1 : 0;
        statement.bindLong(8, _tmp);
        if (entity.getUserAction() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getUserAction());
        }
        if (entity.getActionTimestamp() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getActionTimestamp());
        }
      }
    };
    this.__deletionAdapterOfScanHistoryEntity = new EntityDeletionOrUpdateAdapter<ScanHistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `scan_history` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScanHistoryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM scan_history WHERE scanTimestamp < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final ScanHistoryEntity entity,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfScanHistoryEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final ScanHistoryEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfScanHistoryEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final long cutoff, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cutoff);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ScanHistoryEntity>> observeAll() {
    final String _sql = "SELECT * FROM scan_history ORDER BY scanTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"scan_history"}, new Callable<List<ScanHistoryEntity>>() {
      @Override
      @NonNull
      public List<ScanHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQrUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "qrUrl");
          final int _cursorIndexOfDomainName = CursorUtil.getColumnIndexOrThrow(_cursor, "domainName");
          final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
          final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
          final int _cursorIndexOfAnalysisDetailsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "analysisDetailsJson");
          final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
          final int _cursorIndexOfThreatDetected = CursorUtil.getColumnIndexOrThrow(_cursor, "threatDetected");
          final int _cursorIndexOfUserAction = CursorUtil.getColumnIndexOrThrow(_cursor, "userAction");
          final int _cursorIndexOfActionTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "actionTimestamp");
          final List<ScanHistoryEntity> _result = new ArrayList<ScanHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScanHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQrUrl;
            _tmpQrUrl = _cursor.getString(_cursorIndexOfQrUrl);
            final String _tmpDomainName;
            _tmpDomainName = _cursor.getString(_cursorIndexOfDomainName);
            final float _tmpRiskScore;
            _tmpRiskScore = _cursor.getFloat(_cursorIndexOfRiskScore);
            final String _tmpRiskLevel;
            _tmpRiskLevel = _cursor.getString(_cursorIndexOfRiskLevel);
            final String _tmpAnalysisDetailsJson;
            _tmpAnalysisDetailsJson = _cursor.getString(_cursorIndexOfAnalysisDetailsJson);
            final long _tmpScanTimestamp;
            _tmpScanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
            final boolean _tmpThreatDetected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfThreatDetected);
            _tmpThreatDetected = _tmp != 0;
            final String _tmpUserAction;
            if (_cursor.isNull(_cursorIndexOfUserAction)) {
              _tmpUserAction = null;
            } else {
              _tmpUserAction = _cursor.getString(_cursorIndexOfUserAction);
            }
            final Long _tmpActionTimestamp;
            if (_cursor.isNull(_cursorIndexOfActionTimestamp)) {
              _tmpActionTimestamp = null;
            } else {
              _tmpActionTimestamp = _cursor.getLong(_cursorIndexOfActionTimestamp);
            }
            _item = new ScanHistoryEntity(_tmpId,_tmpQrUrl,_tmpDomainName,_tmpRiskScore,_tmpRiskLevel,_tmpAnalysisDetailsJson,_tmpScanTimestamp,_tmpThreatDetected,_tmpUserAction,_tmpActionTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findByDomain(final String domain,
      final Continuation<? super List<ScanHistoryEntity>> $completion) {
    final String _sql = "SELECT * FROM scan_history WHERE domainName = ? ORDER BY scanTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, domain);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ScanHistoryEntity>>() {
      @Override
      @NonNull
      public List<ScanHistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfQrUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "qrUrl");
          final int _cursorIndexOfDomainName = CursorUtil.getColumnIndexOrThrow(_cursor, "domainName");
          final int _cursorIndexOfRiskScore = CursorUtil.getColumnIndexOrThrow(_cursor, "riskScore");
          final int _cursorIndexOfRiskLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "riskLevel");
          final int _cursorIndexOfAnalysisDetailsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "analysisDetailsJson");
          final int _cursorIndexOfScanTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "scanTimestamp");
          final int _cursorIndexOfThreatDetected = CursorUtil.getColumnIndexOrThrow(_cursor, "threatDetected");
          final int _cursorIndexOfUserAction = CursorUtil.getColumnIndexOrThrow(_cursor, "userAction");
          final int _cursorIndexOfActionTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "actionTimestamp");
          final List<ScanHistoryEntity> _result = new ArrayList<ScanHistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ScanHistoryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpQrUrl;
            _tmpQrUrl = _cursor.getString(_cursorIndexOfQrUrl);
            final String _tmpDomainName;
            _tmpDomainName = _cursor.getString(_cursorIndexOfDomainName);
            final float _tmpRiskScore;
            _tmpRiskScore = _cursor.getFloat(_cursorIndexOfRiskScore);
            final String _tmpRiskLevel;
            _tmpRiskLevel = _cursor.getString(_cursorIndexOfRiskLevel);
            final String _tmpAnalysisDetailsJson;
            _tmpAnalysisDetailsJson = _cursor.getString(_cursorIndexOfAnalysisDetailsJson);
            final long _tmpScanTimestamp;
            _tmpScanTimestamp = _cursor.getLong(_cursorIndexOfScanTimestamp);
            final boolean _tmpThreatDetected;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfThreatDetected);
            _tmpThreatDetected = _tmp != 0;
            final String _tmpUserAction;
            if (_cursor.isNull(_cursorIndexOfUserAction)) {
              _tmpUserAction = null;
            } else {
              _tmpUserAction = _cursor.getString(_cursorIndexOfUserAction);
            }
            final Long _tmpActionTimestamp;
            if (_cursor.isNull(_cursorIndexOfActionTimestamp)) {
              _tmpActionTimestamp = null;
            } else {
              _tmpActionTimestamp = _cursor.getLong(_cursorIndexOfActionTimestamp);
            }
            _item = new ScanHistoryEntity(_tmpId,_tmpQrUrl,_tmpDomainName,_tmpRiskScore,_tmpRiskLevel,_tmpAnalysisDetailsJson,_tmpScanTimestamp,_tmpThreatDetected,_tmpUserAction,_tmpActionTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countByDomain(final String domain,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM scan_history WHERE domainName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, domain);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object totalCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM scan_history";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countByRiskLevel(final String level,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM scan_history WHERE riskLevel = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, level);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByIds(final List<Long> ids, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
        _stringBuilder.append("DELETE FROM scan_history WHERE id IN (");
        final int _inputSize = ids.size();
        StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
        _stringBuilder.append(")");
        final String _sql = _stringBuilder.toString();
        final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
        int _argIndex = 1;
        for (long _item : ids) {
          _stmt.bindLong(_argIndex, _item);
          _argIndex++;
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
