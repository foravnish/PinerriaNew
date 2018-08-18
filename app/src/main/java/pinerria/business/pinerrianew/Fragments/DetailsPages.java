package pinerria.business.pinerrianew.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pinerria.business.pinerrianew.Activites.HomeAct;
import pinerria.business.pinerrianew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsPages extends Fragment {


    public DetailsPages() {
        // Required empty public constructor
    }

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.fav1,
            R.drawable.fav1
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_details_pages, container, false);


        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        // HomeAct.linerFilter.setVisibility(View.VISIBLE);
        HomeAct.title.setText(getArguments().getString("subcategory"));

        for (int i = 0; i < 1; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getActivity()).inflate(R.layout.tab_layout, tabLayout, false);
            TextView tabTextView = (TextView) relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setCompoundDrawablesWithIntrinsicBounds(tabIcons[i], 0, 0, 0);
            tabTextView.setText(tab.getText());
            tab.setCustomView(relativeLayout);
// tab.select();
        }

        TextView tv=(TextView)LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab,null);
        tv.setCompoundDrawablesWithIntrinsicBounds(tabIcons[1], 0, 0, 0);
//// tv.setTypeface(Typeface);
        tabLayout.getTabAt(1).setCustomView(tv);


//        Log.d("dfsdfsdgs",getArguments().getString("id"));
//        Log.d("dfsdffggfgsdgs",getArguments().getString("search"));

        return view;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        //        adapter.addFrag(new DeshAll(), "All"+"("+jsonCount.optString("totalAdv")+")");
//        adapter.addFrag(new Listing(),"LISTING");


//            adapter.addFrag(Details.NewInstance(getArguments().getString("id"),getArguments().getString("jsonArray")), "Details");
//            adapter.addFrag(AddBusiness.NewInstance(getArguments().getString("id")), "Add Business");


        viewPager.setAdapter(adapter);

    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

    }



}
