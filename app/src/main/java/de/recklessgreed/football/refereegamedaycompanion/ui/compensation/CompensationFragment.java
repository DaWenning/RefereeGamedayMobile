package de.recklessgreed.football.refereegamedaycompanion.ui.compensation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.recklessgreed.football.refereegamedaycompanion.databinding.FragmentCompensationBinding;

public class CompensationFragment extends Fragment {

    private FragmentCompensationBinding binding;

    EditText ref_comp, ref_km, ref_km_money, ref_result;
    EditText ump_comp, ump_km, ump_km_money, ump_result;
    EditText lj_comp, lj_km, lj_km_money, lj_result;
    EditText lm_comp, lm_km, lm_km_money, lm_result;
    EditText bj_comp, bj_km, bj_km_money, bj_result;
    EditText sj_comp, sj_km, sj_km_money, sj_result;
    EditText fj_comp, fj_km, fj_km_money, fj_result;
    EditText cj_comp, cj_km, cj_km_money, cj_result;

    EditText totalKm, totalKmMoney, totalComp;

    EditText firstCarKm, firstCarMoney;
    EditText secondCarKm, secondCarMoney;
    EditText thirdCarKm, thirdCarMoney;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CompensationViewModel galleryViewModel =
                new ViewModelProvider(this).get(CompensationViewModel.class);
        binding = FragmentCompensationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ref_comp = binding.refereeCompensation;
        ref_km = binding.refereeKilometers;
        ref_km_money = binding.refereeKilometermoney;
        ref_result = binding.refereeResult;

        ump_comp = binding.umpireCompensation;
        ump_km = binding.umpireKilometers;
        ump_km_money = binding.umpireKilometermoney;
        ump_result = binding.umpireResult;

        lj_comp = binding.linejudgeCompensation;
        lj_km = binding.linejudgeKilometers;
        lj_km_money = binding.linejudgeKilometermoney;
        lj_result = binding.linejudgeResult;

        lm_comp = binding.linesmanCompensation;
        lm_km = binding.linesmanKilometers;
        lm_km_money = binding.linesmanKilometermoney;
        lm_result = binding.linesmanResult;

        bj_comp = binding.backjudgeCompensation;
        bj_km = binding.backjudgeKilometers;
        bj_km_money = binding.backjudgeKilometermoney;
        bj_result = binding.backjudgeResult;

        sj_comp = binding.sidejudgeCompensation;
        sj_km = binding.sidejudgeKilometers;
        sj_km_money = binding.sidejudgeKilometermoney;
        sj_result = binding.sidejudgeResult;

        fj_comp = binding.fieldjudgeCompensation;
        fj_km = binding.fieldjudgeKilometers;
        fj_km_money = binding.fieldjudgeKilometermoney;
        fj_result = binding.fieldjudgeResult;

        cj_comp = binding.centerjudgeCompensation;
        cj_km = binding.centerjudgeKilometers;
        cj_km_money = binding.centerjudgeKilometermoney;
        cj_result = binding.centerjudgeResult;

        totalComp = binding.resultlineResult;
        totalKm = binding.resultlineKilometers;
        totalKmMoney = binding.resultlineKilometermoney;

        firstCarKm = binding.firstCarKm;
        firstCarMoney = binding.firstCarMoney;
        secondCarKm = binding.secondCarKm;
        secondCarMoney = binding.secondCarMoney;
        thirdCarKm = binding.thirdCarKm;
        thirdCarMoney = binding.thirdCarMoney;



        binding.calculateBtn.setOnClickListener((x) -> calculate());




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void calculate() {
        int crewPower = getCrewPower();
        double[] topKms = getTopXKmValues(crewPower > 5 ? 3 : 2);
        double sumKms = Arrays.stream(topKms).sum();
        double allDrivenKms = getAllDrivenKilometer();
        double kmMoney = sumKms * 0.35;

        double completeCompensation = kmMoney + parseDouble(ref_comp) + parseDouble(ump_comp) + parseDouble(lj_comp) + parseDouble(lm_comp)
                + parseDouble(bj_comp) + parseDouble(sj_comp) + parseDouble(fj_comp) + parseDouble(cj_comp);

        if (totalKm != null) totalKm.setText(formatNumber(sumKms));
        if (totalKmMoney != null) totalKmMoney.setText(formatNumber(kmMoney));
        // Gesamt km-money könnte optional angezeigt werden, z.B. in totalComp:
        if (totalComp != null) totalComp.setText(formatNumber(completeCompensation));

        firstCarKm.setText(formatNumber(topKms[0]));
        firstCarMoney.setText(formatNumber(topKms[0] * 0.35));
        secondCarKm.setText(formatNumber(topKms[1]));
        secondCarMoney.setText(formatNumber(topKms[1] * 0.35));
        if (crewPower > 5) {
            thirdCarKm.setText(formatNumber(topKms[2]));
            thirdCarMoney.setText(formatNumber(topKms[2] * 0.35));
        } else {
            thirdCarKm.setText("");
            thirdCarMoney.setText("");
        }


        updateKmMoneyShares(allDrivenKms, kmMoney);
    }

    /**
     * Verteilt kmMoney proportional zu den gefahrenen Kilometern.
     * Setzt jeweils das *_km_money Feld und das *_result Feld (comp + kmAnteil).
     */
    private void updateKmMoneyShares(double totalKms, double kmMoney) {
        if (totalKms <= 0) {
            // Wenn keine Kilometer vorhanden: alles auf 0 setzen
            setMoneyAndResult(ref_km_money, ref_result, 0.0, parseDouble(ref_comp));
            setMoneyAndResult(ump_km_money, ump_result, 0.0, parseDouble(ump_comp));
            setMoneyAndResult(lj_km_money, lj_result, 0.0, parseDouble(lj_comp));
            setMoneyAndResult(lm_km_money, lm_result, 0.0, parseDouble(lm_comp));
            setMoneyAndResult(bj_km_money, bj_result, 0.0, parseDouble(bj_comp));
            setMoneyAndResult(sj_km_money, sj_result, 0.0, parseDouble(sj_comp));
            setMoneyAndResult(fj_km_money, fj_result, 0.0, parseDouble(fj_comp));
            setMoneyAndResult(cj_km_money, cj_result, 0.0, parseDouble(cj_comp));
            return;
        }

        distribute(ref_km, ref_km_money, ref_result, totalKms, kmMoney, ref_comp);
        distribute(ump_km, ump_km_money, ump_result, totalKms, kmMoney, ump_comp);
        distribute(lj_km, lj_km_money, lj_result, totalKms, kmMoney, lj_comp);
        distribute(lm_km, lm_km_money, lm_result, totalKms, kmMoney, lm_comp);
        distribute(bj_km, bj_km_money, bj_result, totalKms, kmMoney, bj_comp);
        distribute(sj_km, sj_km_money, sj_result, totalKms, kmMoney, sj_comp);
        distribute(fj_km, fj_km_money, fj_result, totalKms, kmMoney, fj_comp);
        distribute(cj_km, cj_km_money, cj_result, totalKms, kmMoney, cj_comp);
    }

    private void distribute(EditText kmEt, EditText kmMoneyEt, EditText resultEt,
                            double totalKms, double kmMoney, EditText compEt) {
        double km = parseDouble(kmEt);
        double share = (km <= 0) ? 0.0 : (km / totalKms);
        double shareMoney = share * kmMoney;
        double comp = parseDouble(compEt);
        setMoneyAndResult(kmMoneyEt, resultEt, shareMoney, comp);
    }

    private void setMoneyAndResult(EditText kmMoneyEt, EditText resultEt, double kmMoneyValue, double compValue) {
        if (kmMoneyEt != null) kmMoneyEt.setText(formatNumber(kmMoneyValue));
        if (resultEt != null) resultEt.setText(formatNumber(kmMoneyValue + compValue));
    }

    private double parseDouble(EditText et) {
        if (et == null) return 0.0;
        CharSequence cs = et.getText();
        if (cs == null) return 0.0;
        String s = cs.toString().trim();
        if (s.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String formatNumber(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) return "0";
        if (v == (long) v) return String.valueOf((long) v);
        // Zwei Nachkommastellen für bessere Lesbarkeit
        return String.format(java.util.Locale.getDefault(), "%.2f", v);
    }

    private int getCrewPower() {
        int count = 0;
        if (isNonZero(ref_comp)) count++;
        if (isNonZero(ump_comp)) count++;
        if (isNonZero(lj_comp)) count++;
        if (isNonZero(lm_comp)) count++;
        if (isNonZero(bj_comp)) count++;
        if (isNonZero(sj_comp)) count++;
        if (isNonZero(fj_comp)) count++;
        if (isNonZero(cj_comp)) count++;
        return count;
    }

    public double[] getTopXKmValues(int x) {
        if (x <= 0) return new double[0];
        List<Double> vals = new ArrayList<>();
        collectKm(ref_km, vals);
        collectKm(ump_km, vals);
        collectKm(lj_km, vals);
        collectKm(lm_km, vals);
        collectKm(bj_km, vals);
        collectKm(sj_km, vals);
        collectKm(fj_km, vals);
        collectKm(cj_km, vals);

        if (vals.isEmpty()) return new double[0];

        Collections.sort(vals, Collections.reverseOrder());
        int n = Math.min(x, vals.size());
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = vals.get(i);
        return result;
    }

    public double getAllDrivenKilometer() {
        List<Double> vals = new ArrayList<>();
        collectKm(ref_km, vals);
        collectKm(ump_km, vals);
        collectKm(lj_km, vals);
        collectKm(lm_km, vals);
        collectKm(bj_km, vals);
        collectKm(sj_km, vals);
        collectKm(fj_km, vals);
        collectKm(cj_km, vals);

        if (vals.isEmpty()) return 0;

        return Arrays.stream(vals.toArray(new Double[0])).mapToDouble(Double::doubleValue).sum();
    }

    private void collectKm(EditText et, List<Double> list) {
        if (et == null) return;
        CharSequence cs = et.getText();
        if (cs == null) return;
        String s = cs.toString().trim();
        if (s.isEmpty()) return;
        try {
            double v = Double.parseDouble(s);
            list.add(v);
        } catch (NumberFormatException ignored) { }
    }

    private boolean isNonZero(EditText et) {
        if (et == null) return false;
        CharSequence cs = et.getText();
        if (cs == null) return false;
        String s = cs.toString().trim();
        if (s.isEmpty()) return false;
        try {
            double v = Double.parseDouble(s);
            return v != 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}