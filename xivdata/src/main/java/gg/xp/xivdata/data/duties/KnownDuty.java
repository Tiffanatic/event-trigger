package gg.xp.xivdata.data.duties;

import gg.xp.xivdata.data.*;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum KnownDuty implements Duty {
	None("General", Expansion.GENERAL, DutyType.OTHER),
	Odin("Urth's Fount", 394, Expansion.ARR, DutyType.TRIAL),
	UWU("Weapon's Refrain", 0x309L, Expansion.SB, DutyType.ULTIMATE),
	P1S("P1S", 0x3EB, Expansion.EW, DutyType.SAVAGE_RAID),
	P2S("P2S", 0x3ED, Expansion.EW, DutyType.SAVAGE_RAID),
	P3S("P3S", 0x3EF, Expansion.EW, DutyType.SAVAGE_RAID),
	P4S("P4S", 0x3F1, Expansion.EW, DutyType.SAVAGE_RAID),
	P5N("P5N", 0x439, Expansion.EW, DutyType.RAID),
	P6N("P6N", 0x43B, Expansion.EW, DutyType.RAID),
	P7N("P7N", 0x43D, Expansion.EW, DutyType.RAID),
	P8N("P8N", 0x43F, Expansion.EW, DutyType.RAID),
	P5S("P5S", 0x43A, Expansion.EW, DutyType.SAVAGE_RAID),
	P6S("P6S", 0x43C, Expansion.EW, DutyType.SAVAGE_RAID),
	P7S("P7S", 0x43E, Expansion.EW, DutyType.SAVAGE_RAID),
	P8S("P8S", 0x440, Expansion.EW, DutyType.SAVAGE_RAID),
	EndsingerEx("EX3", 0x3e6, Expansion.EW, DutyType.TRIAL_EX),
	BarbarEx("EX4", 1072, Expansion.EW, DutyType.TRIAL_EX),
	Dragonsong("Dragonsong", 0x3C8, Expansion.EW, DutyType.ULTIMATE),
	ASS_Criterion("Sil'dihn Subterrane (Criterion)", 0x433, Expansion.EW, DutyType.OTHER),
	ASS_Savage("Sil'dihn Subterrane (Savage)", 0x434, Expansion.EW, DutyType.OTHER),
	O1S("O1S", 752, Expansion.SB, DutyType.SAVAGE_RAID),
	O2S("O2S", 753, Expansion.SB, DutyType.SAVAGE_RAID),
	O3S("O3S", 754, Expansion.SB, DutyType.SAVAGE_RAID),
	O4S("O4S", 755, Expansion.SB, DutyType.SAVAGE_RAID),
	O5S("O5S", 695, Expansion.SB, DutyType.SAVAGE_RAID),
	O6S("O6S", 696, Expansion.SB, DutyType.SAVAGE_RAID),
	O7S("O7S", 697, Expansion.SB, DutyType.SAVAGE_RAID),
	O8S("O8S", 698, Expansion.SB, DutyType.SAVAGE_RAID),
	O9S("O9S", 802, Expansion.SB, DutyType.SAVAGE_RAID),
	O10S("O10S", 803, Expansion.SB, DutyType.SAVAGE_RAID),
	O11S("O11S", 804, Expansion.SB, DutyType.SAVAGE_RAID),
	O12S("O12S", 805, Expansion.SB, DutyType.SAVAGE_RAID),
	RubicanteEx("EX5", 1096, Expansion.EW, DutyType.TRIAL_EX),
	Aglaia("Aglaia", 0x41E, Expansion.EW, DutyType.ALLIANCE_RAID),
	Euphrosyne("Euphrosyne", 0x45E, Expansion.EW, DutyType.ALLIANCE_RAID),
	UCoB("Unending Coil of Bahamut", 0x2DD, Expansion.SB, DutyType.ULTIMATE),
	OmegaProtocol("Omega Protocol", 0x462, Expansion.EW, DutyType.ULTIMATE),
	P9N("P9N", 1147, Expansion.EW, DutyType.RAID),
	P10N("P10N", 1149, Expansion.EW, DutyType.RAID),
	P11N("P11N", 1151, Expansion.EW, DutyType.RAID),
	P12N("P12N", 1153, Expansion.EW, DutyType.RAID),
	P9S("P9S", 1148, Expansion.EW, DutyType.SAVAGE_RAID),
	P10S("P10S", 1150, Expansion.EW, DutyType.SAVAGE_RAID),
	P11S("P11S", 1152, Expansion.EW, DutyType.SAVAGE_RAID),
	P12S("P12S", 1154, Expansion.EW, DutyType.SAVAGE_RAID),
	GolbezEx("EX6", 1141, Expansion.EW, DutyType.TRIAL_EX),
	ZeromusEx("EX7", 1169, Expansion.EW, DutyType.TRIAL_EX),

	DtEx1("EX1", 1196, Expansion.DT, DutyType.TRIAL_EX),
	DtEx2("EX2", 1201, Expansion.DT, DutyType.TRIAL_EX),

	M1N("M1N", 1225,Expansion.DT, DutyType.RAID),
	M2N("M2N", 1227,Expansion.DT, DutyType.RAID),
	M3N("M3N", 1229,Expansion.DT, DutyType.RAID),
	M4N("M4N", 1231,Expansion.DT, DutyType.RAID),
	M1S("M1S", 1226,Expansion.DT, DutyType.SAVAGE_RAID),
	M2S("M2S", 1228,Expansion.DT, DutyType.SAVAGE_RAID),
	M3S("M3S", 1230,Expansion.DT, DutyType.SAVAGE_RAID),
	M4S("M4S", 1232,Expansion.DT, DutyType.SAVAGE_RAID),

	M5N("M5N", 1256,Expansion.DT, DutyType.RAID),
	M6N("M6N", 1258,Expansion.DT, DutyType.RAID),
	M7N("M7N", 1260,Expansion.DT, DutyType.RAID),
	M8N("M8N", 1262,Expansion.DT, DutyType.RAID),
	M5S("M5S", 1257,Expansion.DT, DutyType.SAVAGE_RAID),
	M6S("M6S", 1259,Expansion.DT, DutyType.SAVAGE_RAID),
	M7S("M7S", 1261,Expansion.DT, DutyType.SAVAGE_RAID),
	M8S("M8S", 1263,Expansion.DT, DutyType.SAVAGE_RAID),

	FRU("FRU", 1238, Expansion.DT, DutyType.ULTIMATE),
	CodCar("CoD CAR", 1241, Expansion.DT, DutyType.CAR),

	TEA("TEA", 887, Expansion.ShB, DutyType.ULTIMATE)
	;

	private final String name;
	private final Expansion expac;
	private final DutyType type;
	private final @Nullable Long zoneId;

	KnownDuty(String name, long zoneId, Expansion expac, DutyType type) {
		this.name = name;
		this.expac = expac;
		this.type = type;
		this.zoneId = zoneId;
	}

	KnownDuty(String name, Expansion expac, DutyType type) {
		this.name = name;
		this.expac = expac;
		this.type = type;
		this.zoneId = null;
	}

	public static KnownDuty forZone(long zone) {
		return Arrays.stream(values())
				.filter(kd -> kd.zoneId != null && kd.zoneId == zone)
				.findFirst()
				.orElse(null);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Expansion getExpac() {
		return expac;
	}

	@Override
	public DutyType getType() {
		return type;
	}

	@Override
	public @Nullable Long getZoneId() {
		return zoneId;
	}
}
